package dev.nadsonaguiar.CadastroDeNinjas.Missoes;

// LOCALHOST:8080 queremos criar todas para o meu servidor


import org.springframework.web.bind.annotation.*;

@RestController // Ela fala que o nosso código é um Controller, e cria uma rota para as nossas API
@RequestMapping("missoes") // Ela mapeia as nossas API
public class MissoesController {

    //  GET -- Mandar uma requisição para mostrar as missões
    @GetMapping("/listar") // Get serve para devolver/mostrar algo para o usuário
    public String listarMissao(){
        return "Missões listadas com sucesso";
    }

    //  POST -- Mandar uma requisição para criar as missões
    @PostMapping("/criar") // Post em inglês em Postar/Mandar, então essa annotation serve para o usuário mandar uma requester
    public String criarMissao(){
        return "Missão criada com sucesso";
    }

    //  PUT -- Mandar uma requisição para alterar as missões
    @PutMapping("/alterar") // Put serve para alterar
    public String alterarMissao(){
        return "Missão alterada com sucesso";
    }

    //  DELETE -- Mandar uma requisição para deletar as missões
    @DeleteMapping("/deletar") //Delete serve para deletar missão
    public String deletarMissao(){
        return "Missão deletada com sucesso";
    }

}
