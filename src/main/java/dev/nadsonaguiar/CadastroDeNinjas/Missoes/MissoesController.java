package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

// LOCALHOST:8080 queremos criar todas para o meu servidor


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
    public ResponseEntity<MissoesDTO> criar(@Valid @RequestBody MissoesDTO missoes)
    {
        MissoesDTO missoesSalva = missoesService.criarMissao(missoes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(missoesSalva);
    }

    //  GET -- Mandar uma requisição para mostrar as missões
    @GetMapping // Get serve para devolver/mostrar algo para o usuário
    public ResponseEntity<List<MissoesDTO>> listar(){
        List<MissoesDTO> missoes = missoesService.listarMissoes();
        return ResponseEntity.ok(missoes);
    }

    //  GET -- Mandar uma requisição para mostrar as missões, somente pelo ‘ID’
    @GetMapping("/{id}")
    public ResponseEntity<MissoesDTO> buscar(@PathVariable Long id){
        MissoesDTO missao = missoesService.listarMissoesPorId(id);
        if(missao != null){
            return ResponseEntity.ok(missao);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    //  PUT -- Mandar uma requisição para alterar as missões
    @PutMapping("{id}") // Put serve para alterar
    public ResponseEntity<MissoesDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MissoesDTO missaoAtualizada){
        MissoesDTO missao = missoesService.alterarMissao(id, missaoAtualizada);
        if(missao != null){
            return ResponseEntity.ok(missao);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //  DELETE -- Mandar uma requisição para deletar as missões
    @DeleteMapping("/{id}") //Delete serve para deletar missão
    public ResponseEntity<String> deletar(@PathVariable Long id){
        if(missoesService.listarMissoesPorId(id) != null){
            missoesService.deletarMissao(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }

    }

}
