package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissoesDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Dificuldade é obrigatória")
    private String dificuldade;

    @NotBlank(message = "Rank é obrigatório")
    private String rank;


}
