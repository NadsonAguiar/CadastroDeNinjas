package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ninjas")
public class NinjaController {

    //Injeção de dependência para usar NinjaService
    private NinjaService ninjaService;

    public NinjaController(NinjaService ninjaService) {
        this.ninjaService = ninjaService;
    }

    @GetMapping("/boasvindas")
    public String boasVindas(){
        return "Essa é a minha primeira mensagem nessa rota";
    }

    // Adicionar Ninja (CREATE)
    @PostMapping("/criar")
    public NinjaModel criarNinja(@RequestBody NinjaModel ninja) // @RequestBody pega uma requisição do corpo e manda para o BD
    {
        return ninjaService.criarNinja(ninja); // Estamos a fazer uma serialização inversa JSON → Banco de Dados
    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping("/listar")
    public List<NinjaModel> listarNinjas(){
        return ninjaService.listarNinjas();
    }

    // Mostrar Ninja por ID(READ)
    @GetMapping("/listar/{id}") //Usando @PathVariable em {id}
    public NinjaModel listarNinjasPorId(@PathVariable Long id) //@PathVariable = Caminho variável, transforma a variável num caminho para a URL
    {
        return ninjaService.listaNinjasPorId(id); //Id é usado como parâmetro para buscar
    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/alterar/{id}")
    public NinjaModel alterarNinjaPorId(@PathVariable Long id, @RequestBody NinjaModel ninjaAtualizado){
        return  ninjaService.atualizarNinja(id, ninjaAtualizado);
    }

    // Deletar Ninja(DELETE)
    @DeleteMapping("/deletar/{id}")
    public void deletarNinjaPorId(@PathVariable Long id){
            ninjaService.deletarNinjaPorId(id);
    }

}
