package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

// SERIALIZAÇÃO NORMAL- Quando pegamos um dado de um sistema e levando para outro, no nosso caso, pegando do BD e mandando para outro local em arquivo JSON

import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NinjaService {
    //Injeção de dependência para usar NinjaRepository
    private NinjaRepository ninjaRepository;
    private NinjaMapper ninjaMapper;

    public NinjaService(NinjaRepository ninjaRepository, NinjaMapper ninjaMapper) {
        this.ninjaRepository = ninjaRepository;
        this.ninjaMapper = ninjaMapper;
    }

    // Listar todos os meus ninjas
    public List<NinjaModel> listarNinjas(){
        return ninjaRepository.findAll(); // Equivale a SELECT
    }

    // Listar todos os meus ninjas por ID
    public NinjaModel listaNinjasPorId(Long id){
        Optional<NinjaModel> ninjaPorId = ninjaRepository.findById(id); // Equivalente á SELECT * FROM TB_CADASTRO WHERE(findById) id = ?;
        return ninjaPorId.orElse(null); // Função para caso não tenha o ninja selecionado
    }

    // Criar um novo ninja
    public NinjaDTO criarNinja(NinjaDTO ninjaDTO){
        NinjaModel ninjaModel =  ninjaMapper.map(ninjaDTO); // Equivale a INSERT
        ninjaModel = ninjaRepository.save(ninjaModel);
        return ninjaMapper.map(ninjaModel);
    }


    // Deletar um ninja - Tem que ser um metodo VOID
    public void deletarNinjaPorId(Long id){
       ninjaRepository.deleteById(id); // Equivale a DELETE
    }

    // Alterar ninja
    public NinjaModel atualizarNinja(Long id, NinjaModel ninjaAtualizado){
        if (ninjaRepository.existsById(id)){
            ninjaAtualizado.setId(id);
            return ninjaRepository.save(ninjaAtualizado);
        }
        return null;

    }




}
