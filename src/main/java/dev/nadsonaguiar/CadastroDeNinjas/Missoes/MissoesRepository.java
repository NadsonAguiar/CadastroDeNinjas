package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissoesRepository extends JpaRepository<MissoesModel, Long> {

    List<MissoesModel> findByNomeContainingIgnoreCase(String nome);
    List<MissoesModel> findByDificuldade(String dificuldade);
    List<MissoesModel> findByRank(String rank);

}
