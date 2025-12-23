package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Ninjas", description = "Gerenciamento de ninjas")
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
    @Operation(summary = "Cria um ninja", description = "Rota cria um ninja e inseri no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ninja criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na criação do ninja")
    })
    public ResponseEntity<NinjaDTO> criar(
            @Valid @RequestBody NinjaDTO ninja) // @RequestBody pega uma requisição do corpo(JSON) e converte para NinjaDTO
    {
        NinjaDTO ninjaSalvo = ninjaService.criarNinja(ninja); // Estamos a fazer uma serialização inversa JSON → Banco de Dados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ninjaSalvo);

    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping("/todos")
    @Operation(summary = "Lista todos os ninjas", description = "Rota lista todos os ninjas contidos no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ninjas listados com sucesso")
    })
    public ResponseEntity<List<NinjaDTO>> listar(){
        List<NinjaDTO> ninjas = ninjaService.listarNinjas();
        return ResponseEntity.ok(ninjas);

    }

    // Mostra os ninjas paginados
    @GetMapping
    @Operation(summary = "Lista ninjas por paginação", description = "Rota lista os ninja apenas da pagina escolhida")
    public ResponseEntity<Page<NinjaDTO>> listarPorPaginacao(
            @PageableDefault(page = 0,size = 10)
            @ParameterObject
            Pageable pageable){
        Page<NinjaDTO> ninjas = ninjaService.listarNinjasPaginados(pageable);
        return ResponseEntity.ok(ninjas);
    }


    // Mostrar Ninja por ID(READ)
    @GetMapping("/{id}") //Usando @PathVariable em {id}
    @Operation(summary = "Lista um ninja por ID", description = "Rota lista um ninja por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ninja encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ninja não encontrado")
    })
    public ResponseEntity<NinjaDTO> buscar(
            @Parameter(description = "Usuário manda o id no caminho da requisição ")
            @PathVariable Long id) //@PathVariable = Caminho variável, transforma a variável num caminho para a URL
    {
        NinjaDTO ninja = ninjaService.listaNinjasPorId(id); //Id é usado como parâmetro para buscar
        if(ninja != null){
            return ResponseEntity.ok(ninja);
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    // Buscar Ninja por filtro especifico
    @GetMapping("/buscar")
    @Operation(summary = "Buscar ninja por filtros", description = "Busca ninjas por nome, rank ou idade")
    public ResponseEntity<List<NinjaDTO>> buscarPorFiltro(
            @Parameter(description = "Nome (parcial)") @RequestParam(required = false) String nome,
            @Parameter(description = "Rank") @RequestParam(required = false) String rank,
            @Parameter(description = "Idade") @RequestParam(required = false) Integer idade
    ){
        if (nome != null){
            return ResponseEntity.ok(ninjaService.buscarPorNome(nome));
        }
        if (rank != null) {
            return ResponseEntity.ok(ninjaService.buscarPorRank(rank));
        }
        if (idade != null){
           return ResponseEntity.ok(ninjaService.buscarPorIdade(idade));
        }
        return ResponseEntity.ok(ninjaService.listarNinjas());

    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ninja pelo seu ID", description = "Rota atualiza um ninja pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ninja alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ninja não encontrado, não foi possível alterar")
    })
    public ResponseEntity<NinjaDTO> atualizar(
            @Parameter(description = "Usuário manda o id no caminho da requisição")
            @PathVariable Long id,
            @Valid @RequestBody NinjaDTO ninjaAtualizado)
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
    @Operation(summary = "Deleta um ninja pelo seu ID", description = "Rota deleta um ninja pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ninja deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ninja não encontrado, não foi possível deletar")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Usuário manda o ID no caminho da requisição")
            @PathVariable Long id){
        if (ninjaService.listaNinjasPorId(id) != null){
            ninjaService.deletarNinjaPorId(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}
