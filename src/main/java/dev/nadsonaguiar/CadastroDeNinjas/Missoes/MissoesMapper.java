package dev.nadsonaguiar.CadastroDeNinjas.Missoes;


import org.springframework.stereotype.Component;

@Component
public class MissoesMapper {

    // DTO -> MODEL
    public MissoesModel map(MissoesDTO missoesDTO){
        MissoesModel missoesModel = new MissoesModel();
        missoesModel.setId(missoesDTO.getId());
        missoesModel.setNome(missoesDTO.getNome());
        missoesModel.setDificuldade(missoesDTO.getDificuldade());
        missoesModel.setRank(missoesDTO.getRank());

        return missoesModel;

    }


    // MODEL -> DTO
    public MissoesDTO map(MissoesModel missoesModel){
        MissoesDTO missoesDTO = new MissoesDTO();
        missoesDTO.setId(missoesModel.getId());
        missoesDTO.setNome(missoesModel.getNome());
        missoesDTO.setDificuldade(missoesModel.getDificuldade());
        missoesDTO.setRank(missoesModel.getRank());

        return missoesDTO;
    }

}
