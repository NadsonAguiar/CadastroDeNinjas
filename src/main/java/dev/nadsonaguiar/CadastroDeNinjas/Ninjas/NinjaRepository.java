package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import org.springframework.data.jpa.repository.JpaRepository;

// JPA é basicamente um compilado de QUERYS em forma de metodos
public interface NinjaRepository extends JpaRepository<NinjaModel, Long> {
}
