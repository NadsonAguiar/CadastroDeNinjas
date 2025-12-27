package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nadsonaguiar.CadastroDeNinjas.Exception.GlobalExceptionHandler;
import dev.nadsonaguiar.CadastroDeNinjas.Exception.NinjaNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NinjaController.class)
@Import(GlobalExceptionHandler.class)
public class NinjaControllerTest {

    @Autowired
    // MockMvc simula requisições HTTP
    private MockMvc mockMvc;

    @MockitoBean
    //MockitoBean simula um service real
    private NinjaService ninjaService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void deveResponder200AoListarNinjas() throws Exception{
        mockMvc.perform(get("/ninjas"))
                .andExpect(status().isOk());
    }

    // Teste GET para retornar lista de ninjas (200 - sucesso)
    @Test
    void deveRetornarListaDeNinjas() throws Exception{

        // Estamos os valores para a lista falsa
        NinjaDTO ninja1 = new NinjaDTO();
        ninja1.setId(1L);
        ninja1.setNome("Boruto");
        NinjaDTO ninja2 = new NinjaDTO();
        ninja2.setId(2L);
        ninja2.setNome("Sarada");

        // Estamos a colocar os valores dentro da lista falsa
        List<NinjaDTO> ninjas = List.of(ninja1, ninja2);

        // Estamos fazendo when(quando) ninjaservice then(então) retornar a lista
        when(ninjaService.listarNinjas()).thenReturn(ninjas);

        // Aqui estamos a fazer o teste HTTP
        mockMvc.perform(get("/ninjas/todos"))
                // Simula um GET real no endpoint
                .andExpect(status().isOk())
                //"$" significa a raiz do JSON, seguido pelo dado que quero pegar
                .andExpect(jsonPath("$.data.length()").value(2));

    }

    // Teste para quando lista estiver vazia (200 - sucesso)
    @Test
    void deveRetornarListaVaziaQuandoNaoHouverNinjas() throws Exception{

        when(ninjaService.listarNinjas()).thenReturn(List.of());

        mockMvc.perform(get("/ninjas/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucess").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

    }

    // Teste para quando ‘ID’ existir (200 - sucesso)
    @Test
    void deveRetornarNinjaQuandoIdExistir() throws Exception{

        NinjaDTO ninja = new NinjaDTO();
        ninja.setId(1L);
        ninja.setNome("Naruto");

        when(ninjaService.listaNinjasPorId(1L)).thenReturn(ninja);

        mockMvc.perform(get("/ninjas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucess").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Naruto"));

    }

    // Teste para quando 'ID' não existir (404 - não encontrado)
    @Test
    void deveRetornar404QuandoNinjaNaoExistir() throws Exception{

        when(ninjaService.listaNinjasPorId(99L)).thenThrow(new NinjaNotFoundException(99L));

        mockMvc.perform(get("/ninjas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));

    }

    // Teste POST para criar ninja (201 - criado)
    @Test
    void deveCriarNinjaComSucesso() throws Exception{
        // Simula o JSON que o cliente envia
        NinjaDTO ninjaEntrada = new NinjaDTO();
        ninjaEntrada.setNome("Naruto");
        ninjaEntrada.setEmail("naruto@konoha.com");
        ninjaEntrada.setIdade(17);
        ninjaEntrada.setRank("Genin");


        // Simula o DTO de retorno
        NinjaDTO ninjaSalvo = new NinjaDTO();
        ninjaSalvo.setId(1L);
        ninjaSalvo.setNome("Naruto");
        ninjaSalvo.setEmail("naruto@konoha.com");
        ninjaSalvo.setIdade(17);
        ninjaSalvo.setRank("Genin");

        // 🧠 O controller não salva nada, quem salva é o service, então mockamos o comportamento:
        when(ninjaService.criarNinja(any(NinjaDTO.class)))
        // Quando o controller chamar criarNinja(...), devolva esse objeto aqui:
                .thenReturn(ninjaSalvo);

        // MockMvc não entende Java, só JSON, vamos converter Objeto → JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ninjaEntrada);

        // Simular a requisição
        mockMvc.perform(post("/ninjas")
                // contentType → diz que é JSON
                .contentType(MediaType.APPLICATION_JSON)
                // content(json) → corpo da requisição
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucess").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Naruto"));
    }

    // Teste POST para simular validação ao criar ninja (400 - validação)
    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception{
        NinjaDTO ninjaInvalido = new NinjaDTO();
        ninjaInvalido.setNome("");
        ninjaInvalido.setEmail("email-invalido");
        ninjaInvalido.setIdade(-5);
        ninjaInvalido.setRank("");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ninjaInvalido);

        mockMvc.perform(post("/ninjas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // Teste PUT para atualizar um ninja (200 - sucesso)
    @Test
    void deveAtualizarNinja() throws Exception{
        NinjaDTO ninjaSalvo = new NinjaDTO();
        ninjaSalvo.setId(1L);
        ninjaSalvo.setNome("Naruto");
        ninjaSalvo.setEmail("naruto@konoha.com");
        ninjaSalvo.setIdade(18);
        ninjaSalvo.setRank("Jounin");

        when(ninjaService.atualizarNinja(eq(1L), any(NinjaDTO.class))).thenReturn(ninjaSalvo);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ninjaSalvo);

        mockMvc.perform(put("/ninjas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Naruto"))
                .andExpect(jsonPath("$.data.rank").value("Jounin"));
    }

    // Teste PUT para simular um ninja não existente para atualizar (400 - não encontrado)
    @Test
    void deveRetornar404AoAtualizarNinjaInexistente() throws Exception{
        when(ninjaService.atualizarNinja(eq(99L), any(NinjaDTO.class))).thenThrow(new NinjaNotFoundException(99L));

        NinjaDTO ninja = new NinjaDTO();
        ninja.setId(1L);
        ninja.setNome("Naruto");
        ninja.setEmail("naruto@konoha.com");
        ninja.setIdade(18);
        ninja.setRank("Jounin");


        String json = objectMapper.writeValueAsString(ninja);

        mockMvc.perform(put("/ninjas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ninja não encontrado com ID: 99"));
    }

    // Teste DELETE para deletar um ninja (200 - sucesso)
    @Test
    void deveDeletarNinja() throws Exception{
        NinjaDTO ninjaSalvo = new NinjaDTO();
        ninjaSalvo.setId(1L);
        ninjaSalvo.setNome("Naruto");
        ninjaSalvo.setEmail("naruto@konoha.com");
        ninjaSalvo.setIdade(18);
        ninjaSalvo.setRank("Jounin");

        when(ninjaService.listaNinjasPorId(1L)).thenReturn(ninjaSalvo);

        mockMvc.perform(delete("/ninjas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucess").value(true))
                .andExpect(jsonPath("$.message").value("Ninja deletado com sucesso"));
    }
}
