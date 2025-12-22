package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "DTO para criação de uma missão")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissoesDTO {

    private Long id;

    @Schema(description = "Nome da missão", example = "Salvar Vila")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Dificuldade da missão", example = "S")
    @NotBlank(message = "Dificuldade é obrigatória")
    private String dificuldade;

    @Schema(description = "Rank da missão", example = "Hokage")
    @NotBlank(message = "Rank é obrigatório")
    private String rank;


}
