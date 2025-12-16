package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MissoesService {

    //injeção de dependência
    private MissoesMapper missoesMapper;
    private MissoesRepository missoesRepository;

    //construtor para dependências
    public MissoesService(MissoesMapper missoesMapper, MissoesRepository missoesRepository) {
        this.missoesMapper = missoesMapper;
        this.missoesRepository = missoesRepository;
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
            Optional<MissoesModel> missoesPorId = missoesRepository.findById(id);
            return missoesPorId.map(missoesMapper::map).orElse(null);

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

}
