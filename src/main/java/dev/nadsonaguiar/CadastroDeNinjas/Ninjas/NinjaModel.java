package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//Entity ele transforma uma classe em uma entidade BD
// JPA = Java Persistence API
@Entity
@Table(name = "tb_cadastro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "missoes")
public class NinjaModel {

    // Id controla nosso atributo ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera id sequenciais
    @Column (name = "id")
    private Long id;

    @Column (name = "nome")
    private String nome;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column (name = "idade")
    private Integer idade;

    @Column(name = "rank")
    private String rank;

    // @ManyToOne muitos ninjas para uma unica missão
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missoes_id") //Foreing Key ou chave estrangeira
    private MissoesModel missoes;

}
