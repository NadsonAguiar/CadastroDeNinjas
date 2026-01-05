package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import dev.nadsonaguiar.CadastroDeNinjas.Response.ApiSucessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // CRUD - CREATE, READ, UPDATE, DELETE

    // Adicionar Ninja (CREATE)
    @PostMapping
    @Operation(summary = "Cria um ninja", description = "Rota cria um ninja e inseri no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ninja criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na criação do ninja")
    })
    public ResponseEntity<ApiSucessResponse<NinjaDTO>> criar(
            @Valid @RequestBody NinjaDTO ninja) // @RequestBody pega uma requisição do corpo(JSON) e converte para NinjaDTO
    {
        NinjaDTO ninjaSalvo = ninjaService.criarNinja(ninja); // Estamos a fazer uma serialização inversa JSON → Banco de Dados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiSucessResponse.sucess("Ninja criado com sucesso", ninjaSalvo));

    }

    // Mostrar todos os Ninjas(READ)
    @GetMapping("/todos")
    @Operation(summary = "Lista todos os ninjas", description = "Rota lista todos os ninjas contidos no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ninjas listados com sucesso")
    })
    public ResponseEntity<ApiSucessResponse<List<NinjaDTO>>> listar(){
        List<NinjaDTO> ninjas = ninjaService.listarNinjas();
        return ResponseEntity.ok(ApiSucessResponse.sucess(ninjas));

    }

    // Mostra os ninjas paginados
    @GetMapping
    @Operation(summary = "Lista ninjas por paginação", description = "Rota lista os ninja apenas da pagina escolhida")
    public ResponseEntity<PageDTO<NinjaDTO>> listarPorPaginacao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
        )
    {
        Pageable pageable = PageRequest.of(page, size);
        PageDTO<NinjaDTO> ninjas = ninjaService.listarNinjasPaginados(pageable);
        return ResponseEntity.ok(ninjas);
    }


    // Mostrar Ninja por ID(READ)
    @GetMapping("/{id}") //Usando @PathVariable em {id}
    @Operation(summary = "Lista um ninja por ID", description = "Rota lista um ninja por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ninja encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ninja não encontrado")
    })
    public ResponseEntity<ApiSucessResponse<NinjaDTO>> buscar(
            @Parameter(description = "Usuário manda o id no caminho da requisição ")
            @PathVariable Long id) //@PathVariable = Caminho variável, transforma a variável num caminho para a URL
    {
        NinjaDTO ninja = ninjaService.listaNinjasPorId(id); //Id é usado como parâmetro para buscar
        if(ninja != null){
            return ResponseEntity.ok(ApiSucessResponse.sucess(ninja));
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    // Buscar Ninja por filtro especifico(GET)
    @GetMapping("/buscar")
    @Operation(summary = "Buscar ninja por filtros", description = "Busca ninjas por nome, rank ou idade")
    public ResponseEntity<List<NinjaDTO>> buscarPorFiltro(
            @Parameter(description = "Nome (parcial)") @RequestParam(required = false) String nome,
            @Parameter(description = "Rank") @RequestParam(required = false) String rank,
            @Parameter(description = "Idade") @RequestParam(required = false) Integer idade
    ){

        return ResponseEntity.ok(ninjaService.buscarComFiltros(nome, rank, idade));

    }

    // Alterar dados do ninja(UPDATE)
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ninja pelo seu ID", description = "Rota atualiza um ninja pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ninja alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ninja não encontrado, não foi possível alterar")
    })
    public ResponseEntity<ApiSucessResponse<NinjaDTO>> atualizar(
            @Parameter(description = "Usuário manda o id no caminho da requisição")
            @PathVariable Long id,
            @Valid @RequestBody NinjaDTO ninjaAtualizado)
    {
        NinjaDTO ninja = ninjaService.atualizarNinja(id, ninjaAtualizado);

        if(ninja != null){
            return ResponseEntity.ok(ApiSucessResponse.sucess("Ninja atualizado com sucesso", ninja));
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    // Atribuir ninja a uma missão(POST)
    @PutMapping("/{ninjaId}/missao/{missaoId}")
    @Operation(summary = "Atribuir missão a um ninja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Missão atribuída"),
            @ApiResponse(responseCode = "404", description = "Ninja ou missão não encontrados")
    })
    public ResponseEntity<NinjaDTO> atribuirMissao(
            @Parameter(description = "ID do ninja") @PathVariable Long ninjaId,
            @Parameter(description = "ID da missão") @PathVariable Long missaoId
    ){
        NinjaDTO ninja = ninjaService.atribuirMissao(ninjaId, missaoId);
        return ResponseEntity.ok(ninja);
    }

    // Remover ninja de uma missão atribuída(DELETE)
    @DeleteMapping("/{ninjaId}/missao")
    @Operation(summary = "Remover ninja de sua missão")
    public ResponseEntity<NinjaDTO> removerMissao(
            @Parameter(description = "ID do ninja") @PathVariable Long ninjaId
    ){
        NinjaDTO ninja = ninjaService.removerMissao(ninjaId);
        return ResponseEntity.ok(ninja);
    }

    // Deletar Ninja(DELETE)
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um ninja pelo seu ID", description = "Rota deleta um ninja pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ninja deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ninja não encontrado, não foi possível deletar")
    })
    public ResponseEntity<ApiSucessResponse<Void>> deletar(
            @Parameter(description = "Usuário manda o ID no caminho da requisição")
            @PathVariable Long id){
        if (ninjaService.listaNinjasPorId(id) != null){
            ninjaService.deletarNinjaPorId(id);
            return ResponseEntity.ok(ApiSucessResponse.sucess("Ninja deletado com sucesso"));
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}
