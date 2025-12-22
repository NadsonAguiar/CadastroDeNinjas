package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

// LOCALHOST:8080 queremos criar todas para o meu servidor


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Ela fala que o nosso código é um Controller, e cria uma rota para as nossas API
@RequestMapping("/missoes") // Ela mapeia as nossas API
public class MissoesController {

    private MissoesService missoesService;

    public MissoesController(MissoesService missoesService) {
        this.missoesService = missoesService;
    }


    //  POST -- Mandar uma requisição para criar as missões
    @PostMapping // Post em inglês em Postar/Mandar, então essa annotation serve para o usuário mandar uma requester
    @Operation(summary = "Cria uma missão", description = "Rota cria uma missão e inseri no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Missão criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na criação da missão")
    })
    public ResponseEntity<MissoesDTO> criar(@Valid @RequestBody MissoesDTO missoes)
    {
        MissoesDTO missoesSalva = missoesService.criarMissao(missoes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(missoesSalva);
    }

    //  GET -- Mandar uma requisição para mostrar as missões
    @GetMapping // Get serve para devolver/mostrar algo para o usuário
    @Operation(summary = "Lista todas as missões", description = "Rota lista todas as missões contidas no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Missões listadas com sucesso")
    })
    public ResponseEntity<List<MissoesDTO>> listar(){
        List<MissoesDTO> missoes = missoesService.listarMissoes();
        return ResponseEntity.ok(missoes);
    }

    //  GET -- Mandar uma requisição para mostrar as missões, somente pelo ‘ID’
    @GetMapping("/{id}")
    @Operation(summary = "Lista uma missão por ID", description = "Rota lista uma missão por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Missão encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<MissoesDTO> buscar(
            @Parameter(description = "Usuário manda o ID no caminho da requisição")
            @PathVariable Long id){
        MissoesDTO missao = missoesService.listarMissoesPorId(id);
        if(missao != null){
            return ResponseEntity.ok(missao);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    //  PUT -- Mandar uma requisição para alterar as missões
    @PutMapping("{id}") // Put serve para alterar
    @Operation(summary = "Atualiza uma missão pelo seu ID", description = "Rota atualiza uma missão pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Missão alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Missão não encontrada, não foi possível alterar")
    })
    public ResponseEntity<MissoesDTO> atualizar(
            @Parameter(description = "Usuário manda o ID no caminho da requisição")
            @PathVariable Long id,
            @Valid @RequestBody MissoesDTO missaoAtualizada){
        MissoesDTO missao = missoesService.alterarMissao(id, missaoAtualizada);
        if(missao != null){
            return ResponseEntity.ok(missao);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //  DELETE -- Mandar uma requisição para deletar as missões
    @DeleteMapping("/{id}") //Delete serve para deletar missão
    @Operation(summary = "Deleta uma missão pelo seu ID", description = "Rota deleta uma missão pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Missão deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada, não foi possível deletar")
    })
    public ResponseEntity<String> deletar(
            @Parameter(description = "Usuário manda o ID no caminho da requisição")
            @PathVariable Long id){
        if(missoesService.listarMissoesPorId(id) != null){
            missoesService.deletarMissao(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }

    }

}
