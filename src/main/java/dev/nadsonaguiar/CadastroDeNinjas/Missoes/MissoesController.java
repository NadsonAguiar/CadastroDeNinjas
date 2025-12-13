package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

// LOCALHOST:8080 queremos criar todas para o meu servidor


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

    //  GET -- Mandar uma requisição para mostrar as missões
    @GetMapping("/listar") // Get serve para devolver/mostrar algo para o usuário
    public List<MissoesDTO> listarMissoes(){
        List<MissoesDTO> missoes = missoesService.listarMissoes();
        return missoes;
    }

    //  POST -- Mandar uma requisição para criar as missões
    @PostMapping("/criar") // Post em inglês em Postar/Mandar, então essa annotation serve para o usuário mandar uma requester
    public ResponseEntity<String> criarMissao(@RequestBody MissoesDTO missoes)
    {
        MissoesDTO missoesSalva = missoesService.criarMissao(missoes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Missão criada com sucesso: " + missoesSalva.getNome() + " (ID): " + missoesSalva.getId());
    }

    //  PUT -- Mandar uma requisição para alterar as missões
    @PutMapping("/alterar/{id}") // Put serve para alterar
    public ResponseEntity<?> alterarMissaoPorId(@PathVariable Long id, @RequestBody MissoesDTO missaoAtualizada){

        MissoesDTO missao = missoesService.alterarMissao(id, missaoAtualizada);
        if(missao != null){
            return ResponseEntity.ok(missao);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Missao com id " + id + " não existe nos nossos registros");
        }
    }

    //  DELETE -- Mandar uma requisição para deletar as missões
    @DeleteMapping("/deletar/{id}") //Delete serve para deletar missão
    public void deletarMissao(@PathVariable Long id){
        missoesService.deletarMissao(id);
    }

}
