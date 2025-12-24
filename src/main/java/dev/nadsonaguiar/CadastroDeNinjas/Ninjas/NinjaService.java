package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

// SERIALIZAÇÃO NORMAL- Quando pegamos um dado de um sistema e levando para outro, no nosso caso, pegando do BD e mandando para outro local em arquivo JSON

import dev.nadsonaguiar.CadastroDeNinjas.Exception.MissaoNotFoundException;
import dev.nadsonaguiar.CadastroDeNinjas.Exception.NinjaNotFoundException;
import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesModel;
import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesRepository;
import dev.nadsonaguiar.CadastroDeNinjas.Specification.NinjaSpecification;
import jakarta.persistence.Id;
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
    public NinjaDTO criarNinja(NinjaDTO ninjaDTO){
        NinjaModel ninjaModel =  ninjaMapper.map(ninjaDTO); // Equivale a INSERT
        ninjaModel = ninjaRepository.save(ninjaModel);
        return ninjaMapper.map(ninjaModel);
    }

    // Listar todos os meus ninjas
    public List<NinjaDTO> listarNinjas(){
        List<NinjaModel> ninjas = ninjaRepository.findAll(); // Equivale a SELECT
        return ninjas.stream()
                .map(ninjaMapper::map)
                .collect(Collectors.toList());
    }

    // Listar os meus ninjas por ID
    public NinjaDTO listaNinjasPorId(Long id)
    {
        // Equivalente á SELECT * FROM TB_CADASTRO WHERE(findById) id = ?;
        return ninjaRepository.findById(id)
                .map(ninjaMapper::map)
                .orElseThrow(() -> new NinjaNotFoundException(id));
                // Função para caso não tenha o ninja selecionado
    }

    // Lista ninjas por paginação
    public Page<NinjaDTO> listarNinjasPaginados(Pageable pageable){
        Page<NinjaModel> ninjaPage = ninjaRepository.findAll(pageable);
        return ninjaPage.map(ninjaMapper::map);
    }

    // Alterar ninja
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
    public void deletarNinjaPorId(Long id)
    {
       ninjaRepository.deleteById(id); // Equivale a DELETE
    }

    // Buscar por filtros
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
