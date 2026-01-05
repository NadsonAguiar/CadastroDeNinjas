package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

// SERIALIZAÇÃO NORMAL- Quando pegamos um dado de um sistema e levando para outro, no nosso caso, pegando do BD e mandando para outro local em arquivo JSON

import dev.nadsonaguiar.CadastroDeNinjas.Exception.MissaoNotFoundException;
import dev.nadsonaguiar.CadastroDeNinjas.Exception.NinjaNotFoundException;
import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesModel;
import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesRepository;
import dev.nadsonaguiar.CadastroDeNinjas.Specification.NinjaSpecification;
import jakarta.persistence.Id;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NinjaService {
    //Injeção de dependência para usar NinjaRepository e NinjaMapper
    private final NinjaRepository ninjaRepository;
    private final NinjaMapper ninjaMapper;
    private final MissoesRepository missoesRepository;

    public NinjaService(NinjaRepository ninjaRepository, NinjaMapper ninjaMapper, MissoesRepository missoesRepository) {
        this.ninjaRepository = ninjaRepository;
        this.ninjaMapper = ninjaMapper;
        this.missoesRepository = missoesRepository;
    }

    // Criar um novo ninja
    @Caching(evict = {
            @CacheEvict(value = {"ninjas-page", "ninjas-filter", "ninjas-list"}, allEntries = true) // Apaga tudo do cache, já que quando um ‘item’ muda, lista fica desatualizada
    })
    public NinjaDTO criarNinja(NinjaDTO ninjaDTO){
        NinjaModel ninjaModel =  ninjaMapper.map(ninjaDTO);
        ninjaModel = ninjaRepository.save(ninjaModel); // Equivale a INSERT
        return ninjaMapper.map(ninjaModel);
    }

    // Listar todos os meus ninjas
    @Cacheable("ninjas-list") // Realiza a busca uma vez no cacheName -> (key -> value), ex: "ninjasList" → (1 → NinjaDTO), se não tiver, executa o metodo e vai no Banco
    public List<NinjaDTO> listarNinjas(){
        List<NinjaModel> ninjas = ninjaRepository.findAll(); // Equivale a SELECT *
        return ninjas.stream()
                .map(ninjaMapper::map)
                .collect(Collectors.toList());
    }

    // Listar os meus ninjas por ID
    @Cacheable(value = "ninjas-by-id", key = "#id", unless = "#result == null", condition = "#id >=0")/* Realiza a busca uma vez no cacheName -> (key -> value), ex: "ninjas" → (key: ninjas::1 → valor: NinjaDTO),
     se não tiver, executa o metodo e vai no Banco, o "unless" serve para decidir após executar, caso o “id” seja null no banco, não salva resultado no cache,
     já o "condition" decide antes da consulta, caso o "id" seja menor que 0 nem usa cache */
    public NinjaDTO listaNinjasPorId(Long id)
    {
        // Equivalente á SELECT * FROM TB_CADASTRO WHERE(findById) id = ?;
        return ninjaRepository.findById(id)
                .map(ninjaMapper::map)
                .orElseThrow(() -> new NinjaNotFoundException(id));
                // Função para caso não tenha o ninja selecionado
    }

    // Lista ninjas por paginação
    @Cacheable(
            value = "ninjas-page",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort" // cada combinação vai ser "ninjas-page::0(pg)-10(size)-nome,asc"
    )
    public PageDTO<NinjaDTO> listarNinjasPaginados(Pageable pageable){
        Page<NinjaModel> ninjaPage = ninjaRepository.findAll(pageable);
        // Converte Page<NinjaModel> para Page<NinjaDTO>
        Page<NinjaDTO> dtoPage = ninjaPage.map(ninjaMapper::map);
        return new PageDTO<>(dtoPage);
    }

    // Buscar por filtros
    @Cacheable(
            value = "ninjas-filter",
            key = "T(java.util.Objects).hash(#nome, #rank, #idade)", // Regra de ouro para Specification + Cache: Toda a variação lógica da query precisa gerar uma variação única de cache
            condition = "#nome != null || #rank != null || #idade != null",
            unless = "#result.isEmpty()"
    )
    public List<NinjaDTO> buscarComFiltros(String nome, String rank, Integer idade){
        Specification<NinjaModel> spec = Specification.allOf(
                NinjaSpecification.nomeLike(nome),
                NinjaSpecification.rankEquals(rank),
                NinjaSpecification.idadeEquals(idade)
        );
        return ninjaRepository.findAll(spec)
                .stream()
                .map(ninjaMapper::map)
                .toList();

    }

    // Alterar ninja
    @Caching(put = @CachePut(value = "ninja", key = "#id"), // Caching serve para forma combos, CachePut diferente do Cacheable, ele sempre executa o metodo, atualiza o banco e cache
            evict = {
            @CacheEvict(value = {"ninjas-by-id","ninjas-list","ninjas-page", "ninjas-filter"}, allEntries = true) // CacheEvict Apaga tudo, se usado em combo(Caching), nesse caso PUT, atualiza cache individual e invalida cache da lista
    })
    public NinjaDTO atualizarNinja(Long id, NinjaDTO ninjaDTO)
    {
        Optional<NinjaModel> ninjaExistente = ninjaRepository.findById(id);
        if (ninjaExistente.isPresent()){
            NinjaModel ninjaAtualizado = ninjaMapper.map(ninjaDTO);
            ninjaAtualizado.setId(id);
            NinjaModel ninjaSalvo = ninjaRepository.save(ninjaAtualizado);
            return  ninjaMapper.map(ninjaSalvo);
        }
        return null;
    }

    // Deletar um ninja - Tem que ser um metodo VOID
    @CacheEvict(value = {"ninjas-by-id","ninjas-list","ninjas-page", "ninjas-filter"}, allEntries = true) // CacheEvict = “Depois desse metodo, apague esse cache”
    public void deletarNinjaPorId(Long id)
    {
       ninjaRepository.deleteById(id); // Equivale a DELETE
    }

    // Atribuir ninja a uma missão
    @Transactional
    public NinjaDTO atribuirMissao(Long ninjaId, Long missaoId){
        NinjaModel ninja = ninjaRepository.findById(ninjaId)
                .orElseThrow(() -> new NinjaNotFoundException(ninjaId));
        MissoesModel missao = missoesRepository.findById(missaoId)
                .orElseThrow(() -> new MissaoNotFoundException(missaoId));
        ninja.setMissoes(missao);
        NinjaModel salvo = ninjaRepository.save(ninja);
        return ninjaMapper.map(salvo);

    }

    // Remover ninja de uma missão
    @Transactional
    public NinjaDTO removerMissao(Long ninjaID){
        NinjaModel ninja = ninjaRepository.findById(ninjaID)
                .orElseThrow(() -> new NinjaNotFoundException(ninjaID));
        ninja.setMissoes(null);
        NinjaModel salvo = ninjaRepository.save(ninja);
        return ninjaMapper.map(salvo);
    }

}
