package dev.nadsonaguiar.CadastroDeNinjas.Exception;

public class NinjaNotFoundException extends  RuntimeException{

    public NinjaNotFoundException(Long id){
        super("Ninja não encontrado com ID: " + id);
    }




}
