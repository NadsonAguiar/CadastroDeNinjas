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



    // Adicionar Ninja (CREATE)
    @PostMapping
    public ResponseEntity<NinjaDTO> criar(@Valid @RequestBody NinjaDTO ninja) // @RequestBody pega uma requisição do corpo(JSON) e converte para NinjaDTO
    {
        NinjaDTO ninjaSalvo = ninjaService.criarNinja(ninja); // Estamos a fazer uma serialização inversa JSON → Banco de Dados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ninjaSalvo);

    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping
    public ResponseEntity<List<NinjaDTO>> listar(){
        List<NinjaDTO> ninjas = ninjaService.listarNinjas();
        return ResponseEntity.ok(ninjas);

    }

    // Mostrar Ninja por ID(READ)
    @GetMapping("/{id}") //Usando @PathVariable em {id}
    public ResponseEntity<NinjaDTO> buscar(@PathVariable Long id) //@PathVariable = Caminho variável, transforma a variável num caminho para a URL
    {
        NinjaDTO ninja = ninjaService.listaNinjasPorId(id); //Id é usado como parâmetro para buscar
        if(ninja != null){
            return ResponseEntity.ok(ninja);
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<NinjaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody NinjaDTO ninjaAtualizado)
    {
        NinjaDTO ninja = ninjaService.atualizarNinja(id, ninjaAtualizado);

        if(ninja != null){
            return ResponseEntity.ok(ninja);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar Ninja(DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if (ninjaService.listaNinjasPorId(id) != null){
            ninjaService.deletarNinjaPorId(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}
