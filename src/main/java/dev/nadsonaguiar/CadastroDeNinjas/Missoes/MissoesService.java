package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import dev.nadsonaguiar.CadastroDeNinjas.Exception.MissaoNotFoundException;
import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaDTO;
import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MissoesService {

    //injeção de dependência
    private final MissoesMapper missoesMapper;
    private final MissoesRepository missoesRepository;
    private final NinjaMapper ninjaMapper;

    //construtor para dependências
    public MissoesService(MissoesMapper missoesMapper, MissoesRepository missoesRepository, NinjaMapper ninjaMapper) {
        this.missoesMapper = missoesMapper;
        this.missoesRepository = missoesRepository;
        this.ninjaMapper = ninjaMapper;
    }

    //Criar missao
    public MissoesDTO criarMissao(MissoesDTO missoesDTO){
        // 1. DTO → Model (preparar para salvar)
        MissoesModel missoesModel = missoesMapper.map(missoesDTO);
        // 2. Salvar no banco (INSERT)
        missoesModel = missoesRepository.save(missoesModel);
        // 3. Model → DTO (preparar resposta)
        return missoesMapper.map(missoesModel);

    }

    // Listar missao
    public List<MissoesDTO> listarMissoes(){
        // 1. Busca TODAS as missões no banco
        List<MissoesModel> missoes =  missoesRepository.findAll();
        // 2. Converte cada MissoesModel em MissoesDTO
        return missoes.stream()
                // Converte cada MissoesModel em MissoesDTO
               .map(missoesMapper::map)
                // Junta tudo numa lista imutável
               .toList();
    }

    // Listar missao por ID
    public MissoesDTO listarMissoesPorId(Long id){

        return missoesRepository.findById(id)
                .map(missoesMapper::map)
                .orElseThrow(() -> new MissaoNotFoundException(id));

    }

    // Listar Missoes Paginadas
    public Page<MissoesDTO> listarMissoesPaginado(Pageable pageable){
       Page<MissoesModel> missaoPage = missoesRepository.findAll(pageable);
               return missaoPage.map(missoesMapper::map);
    }

    // Alterar missao
    public MissoesDTO alterarMissao(Long id, MissoesDTO missoesDTO){
        Optional<MissoesModel> missaoExistente = missoesRepository.findById(id);
        if (missaoExistente.isPresent()){
            // 1. DTO → Model (preparar para salvar)
            MissoesModel missaoAtualizada = missoesMapper.map(missoesDTO);
            missaoAtualizada.setId(id);
            // 2. Salva a missão atualizada no Banco de Dados
            MissoesModel missaoSalva = missoesRepository.save(missaoAtualizada);
            // 3. Model → DTO
            return missoesMapper.map(missaoSalva);
        }
        return null;
    }

    // Deletar missao
    public void deletarMissao(Long id){
        missoesRepository.deleteById(id);
    }

    // Buscar por nome
    public List<MissoesDTO> buscarPorNome(String nome){
        return missoesRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(missoesMapper::map)
                .toList();
    }

    // Buscar por rank
    public List<MissoesDTO> buscarPorRank(String rank){
        return missoesRepository.findByRank(rank)
                .stream()
                .map(missoesMapper::map)
                .toList();
    }

    // Buscar por idade
    public List<MissoesDTO> buscarPorDificuldade(String dificuldade){
        return missoesRepository.findByDificuldade(dificuldade)
                .stream()
                .map(missoesMapper::map)
                .toList();
    }

    // Listar ninjas de uma missão
    public List<NinjaDTO> listarNinjasDaMissao(Long missaoId){
        MissoesModel missao = missoesRepository.findById(missaoId)
                .orElseThrow(() -> new MissaoNotFoundException(missaoId));
        return missao.getNinjas()
                .stream()
                .map(ninjaMapper::map)
                .toList();
    }


}
