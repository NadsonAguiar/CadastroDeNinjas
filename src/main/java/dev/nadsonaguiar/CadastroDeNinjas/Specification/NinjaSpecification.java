package dev.nadsonaguiar.CadastroDeNinjas.Specification;

import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaModel;
import org.springframework.data.jpa.domain.Specification;

public class NinjaSpecification {
    public static Specification<NinjaModel> nomeLike(String nome) {
        // root = Representa a tabela (entidade)
        // query = Representa o SELECT
        // cb = Ferramenta para montar condições
        return (root, query, cb) ->
                nome == null ? null :
                        cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }
    public static Specification<NinjaModel> rankEquals(String rank) {
        return (root, query, cb) ->
                rank == null ? null :
                        cb.equal(root.get("rank"), rank);
    }

    public static Specification<NinjaModel> idadeEquals(Integer idade) {
        return (root, query, cb) ->
                idade == null ? null :
                        cb.equal(root.get("idade"), idade);
    }
}
