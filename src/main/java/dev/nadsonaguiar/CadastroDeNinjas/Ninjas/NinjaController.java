package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ninjas")
public class NinjaController {

    //Injeção de dependência para usar NinjaService
    private final NinjaService ninjaService;

    public NinjaController(NinjaService ninjaService) {
        this.ninjaService = ninjaService;
    }

    @GetMapping("/boasvindas")
    public String boasVindas(){
        return "Essa é a minha primeira mensagem nessa rota";
    }

    // Adicionar Ninja (CREATE)
    @PostMapping("/criar")
    public ResponseEntity<String> criarNinja(@Valid @RequestBody NinjaDTO ninja) // @RequestBody pega uma requisição do corpo(JSON) e converte para NinjaDTO
    {
        NinjaDTO ninjaSalvo = ninjaService.criarNinja(ninja); // Estamos a fazer uma serialização inversa JSON → Banco de Dados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Ninja criado com sucesso: " + ninjaSalvo.getNome() + " (ID): " + ninjaSalvo.getId());

    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping("/listar")
    public ResponseEntity<List<NinjaDTO>> listarNinjas(){
        List<NinjaDTO> ninjas = ninjaService.listarNinjas();
        return ResponseEntity.ok(ninjas);

    }

    // Mostrar Ninja por ID(READ)
    @GetMapping("/listar/{id}") //Usando @PathVariable em {id}
    public ResponseEntity<?> listarNinjasPorId(@PathVariable Long id) //@PathVariable = Caminho variável, transforma a variável num caminho para a URL
    {
        NinjaDTO ninja = ninjaService.listaNinjasPorId(id); //Id é usado como parâmetro para buscar
        if(ninja != null){
            return ResponseEntity.ok(ninja);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ninja com id " + id + " não existe nos nossos registros");
        }

    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alterarNinjaPorId(@PathVariable Long id, @Valid @RequestBody NinjaDTO ninjaAtualizado)
    {
        NinjaDTO ninja = ninjaService.atualizarNinja(id, ninjaAtualizado);

        if(ninja != null){
            return ResponseEntity.ok(ninja);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ninja com id " + id + " não existe nos nossos registros");
        }
    }

    // Deletar Ninja(DELETE)
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarNinjaPorId(@PathVariable Long id){
        if (ninjaService.listaNinjasPorId(id) != null){
            ninjaService.deletarNinjaPorId(id);
            return ResponseEntity.ok("Ninja com o ID " + id + " deletado com sucesso");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ninja com id " + id + " não existe nos nossos registros");
        }

    }

}
