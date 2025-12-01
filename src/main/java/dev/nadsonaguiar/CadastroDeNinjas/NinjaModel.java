package dev.nadsonaguiar.CadastroDeNinjas;

import jakarta.persistence.*;

//Entity ele transforma uma classe em uma entidade BD
// JPA = Java Persistence API
@Entity
@Table(name = "tb_cadastro")
public class NinjaModel {

    // Id controla nosso atributo ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera id sequenciais
    Long id;
    private String nome;
    private String email;
    private int idade;


    public NinjaModel() {
    }

    public NinjaModel(String nome, String email, int idade) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
