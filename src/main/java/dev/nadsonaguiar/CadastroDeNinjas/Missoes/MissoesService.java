package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import org.springframework.stereotype.Service;

@Service
public class MissoesService {

    private MissoesMapper missoesMapper;
    private MissoesRepository missoesRepository;

    public MissoesService(MissoesMapper missoesMapper, MissoesRepository missoesRepository) {
        this.missoesMapper = missoesMapper;
        this.missoesRepository = missoesRepository;
    }

    public MissoesDTO criarMissao(MissoesDTO missoesDTO){
        // 1. DTO → Model (preparar para salvar)
        MissoesModel missoesModel = missoesMapper.map(missoesDTO);
        // 2. Salvar no banco (INSERT)
        missoesModel = missoesRepository.save(missoesModel);
        // 3. Model → DTO (preparar resposta)
        return missoesMapper.map(missoesModel);

    }

}
