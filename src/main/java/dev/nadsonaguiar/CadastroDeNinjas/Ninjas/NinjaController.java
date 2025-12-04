package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ninjas")
public class NinjaController {


    @GetMapping("/boasvindas")
    public String boasVindas(){
        return "Essa é a minha primeira mensagem nessa rota";
    }

    // Adicionar Ninja (CREATE)
    @PostMapping("/criar")
    public String criarNinja(){
        return "Ninja criado";
    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping("/listar")
    public String mostrarTodosOsNinjas(){
        return "Mostrar Ninja";
    }

    // Mostrar Ninja por ID(READ)
    @GetMapping("/listarID")
    public String mostrarTodosOsNinjasPorId(){
        return "Mostrar Ninja por ID";
    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/alterarID")
    public String alterarNinjaPorId(){
        return "Alterar Ninja por ID";
    }

    // Deletar Ninja(DELETE)
    @DeleteMapping("/deletarID")
    public String deletarNinjaPorId(){
        return "Ninja deletado por ID";
    }

}
