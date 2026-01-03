package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import dev.nadsonaguiar.CadastroDeNinjas.Missoes.MissoesModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Schema(description = "DTO para criação de Ninja")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NinjaDTO{

    private Long id;
    @Schema(description = "Nome do ninja", example = "Naruto Uzumaki")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Email do ninja", example = "naruto@teste.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Schema(description = "Idade do ninja", example = "15")
    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser positiva")
    private Integer idade;

    @Schema(description = "Rank do ninja", example = "Genin")
    @NotBlank(message = "Rank é obrigatório")
    private String rank;

    @Schema(description = "Missão atribuída ao ninja")
    private MissoesModel missoes;




}
