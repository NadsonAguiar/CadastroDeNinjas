package dev.nadsonaguiar.CadastroDeNinjas.Exception;

public class MissaoNotFoundException extends  RuntimeException{
    public MissaoNotFoundException(Long id){
        super("Missão não encontrada com ID: " + id);
    }
}
