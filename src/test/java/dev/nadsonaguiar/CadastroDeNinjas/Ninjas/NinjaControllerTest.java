package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nadsonaguiar.CadastroDeNinjas.Exception.NinjaNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NinjaController.class)
public class NinjaControllerTest {

    @Autowired
    // MockMvc simula requisições HTTP
    private MockMvc mockMvc;

    @MockitoBean
    //MockitoBean simula um service real
    private NinjaService ninjaService;


    @Test
    void deveResponder200AoListarNinjas() throws Exception{
        mockMvc.perform(get("/ninjas"))
                .andExpect(status().isOk());
    }

    // Teste de retorna lista ninja
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

    // Teste para quando lista estiver vazia
    @Test
    void deveRetornarListaVaziaQuandoNaoHouverNinjas() throws Exception{

        when(ninjaService.listarNinjas()).thenReturn(List.of());

        mockMvc.perform(get("/ninjas/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucess").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

    }

    // Teste para quando ‘ID’ existir
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

    // Teste para quando 'ID' não existir
    @Test
    void deveRetornar404QuandoNinjaNaoExistir() throws Exception{

        when(ninjaService.listaNinjasPorId(99L)).thenThrow(new NinjaNotFoundException(99L));

        mockMvc.perform(get("/ninjas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));

    }

    // Teste POST para criar ninja
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
}
