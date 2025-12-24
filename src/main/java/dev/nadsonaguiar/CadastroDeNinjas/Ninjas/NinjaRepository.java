package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

// JPA é basicamente um compilado de QUERYS em forma de metodos
public interface NinjaRepository extends JpaRepository<NinjaModel, Long>, JpaSpecificationExecutor<NinjaModel>
{
    List<NinjaModel> findByNomeContainingIgnoreCase(String nome);
    List<NinjaModel> findByRank(String rank);
    List<NinjaModel> findByIdade(Integer idade);



}
