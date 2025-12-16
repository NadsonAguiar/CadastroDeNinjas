package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

// SERIALIZAÇÃO NORMAL- Quando pegamos um dado de um sistema e levando para outro, no nosso caso, pegando do BD e mandando para outro local em arquivo JSON

import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NinjaService {
    //Injeção de dependência para usar NinjaRepository e NinjaMapper
    private final NinjaRepository ninjaRepository;
    private final NinjaMapper ninjaMapper;

    public NinjaService(NinjaRepository ninjaRepository, NinjaMapper ninjaMapper) {
        this.ninjaRepository = ninjaRepository;
        this.ninjaMapper = ninjaMapper;
    }

    // Criar um novo ninja
    public NinjaDTO criarNinja(NinjaDTO ninjaDTO){
        NinjaModel ninjaModel =  ninjaMapper.map(ninjaDTO); // Equivale a INSERT
        ninjaModel = ninjaRepository.save(ninjaModel);
        return ninjaMapper.map(ninjaModel);
    }

    // Listar todos os meus ninjas
    public List<NinjaDTO> listarNinjas(){
        List<NinjaModel> ninjas = ninjaRepository.findAll(); // Equivale a SELECT
        return ninjas.stream()
                .map(ninjaMapper::map)
                .collect(Collectors.toList());
    }

    // Listar os meus ninjas por ID
    public NinjaDTO listaNinjasPorId(Long id)
    {
        Optional<NinjaModel> ninjaPorId = ninjaRepository.findById(id); // Equivalente á SELECT * FROM TB_CADASTRO WHERE(findById) id = ?;
        return ninjaPorId.map(ninjaMapper::map).orElse(null); // Função para caso não tenha o ninja selecionado
    }

    // Alterar ninja
    public NinjaDTO atualizarNinja(Long id, NinjaDTO ninjaDTO)
    {
        Optional<NinjaModel> ninjaExistente = ninjaRepository.findById(id);
        if (ninjaExistente.isPresent()){
            NinjaModel ninjaAtualizado = ninjaMapper.map(ninjaDTO);
            ninjaAtualizado.setId(id);
            NinjaModel ninjaSalvo = ninjaRepository.save(ninjaAtualizado);
            return  ninjaMapper.map(ninjaSalvo);
        }
        return null;
    }

    // Deletar um ninja - Tem que ser um metodo VOID
    public void deletarNinjaPorId(Long id)
    {
       ninjaRepository.deleteById(id); // Equivale a DELETE
    }





}
