package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

// SERIALIZATION - Quando pegamos um dado de um sistema e levando para outro, no nosso caso, pegando do BD e mandando para outro local em arquivo JSON

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NinjaService {
    //Injeção de dependência para usar NinjaRepository
    private NinjaRepository ninjaRepository;

    public NinjaService(NinjaRepository ninjaRepository) {
        this.ninjaRepository = ninjaRepository;
    }

    // Listar todos os meus ninjas
    public List<NinjaModel> listarNinjas(){
        return ninjaRepository.findAll(); // Equivale a SELECT
//        return ninjaRepository.save(); // Equivale a INSERT
    }
}
