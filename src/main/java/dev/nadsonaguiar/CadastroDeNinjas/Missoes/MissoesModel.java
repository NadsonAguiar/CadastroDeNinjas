package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_missoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissoesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String dificuldade;

    private String rank;

    







    // @OneToMany uma missão pode ter vários ninjas
    /* Quando usarmos o NinjaController para listar os ninjas, vai dar um erro de "loop de serialização", devido à linha abaixo
    private List<NinjaModel> ninjas, por isso vamos ter que usar a annotation @JsonIgnore */
    // @OneToMany uma missão pode ter vários ninjas
    @OneToMany(mappedBy = "missoes")
    @JsonIgnore
    private List<NinjaModel> ninjas;


}
