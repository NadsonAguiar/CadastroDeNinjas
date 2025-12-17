package dev.nadsonaguiar.CadastroDeNinjas.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


// Classe criada para tratar exceptions
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Diz que esse metodo trata erro de validação
    @ExceptionHandler
    public ResponseEntity<?> exception(MethodArgumentNotValidException ex)
    {

        Map<String, String> erros = new HashMap<>();
        // getBindingResult pega o relatório da validação
        ex.getBindingResult()
                // getFieldErrors = lista de campos com erro
                .getFieldErrors()
                // getField = nome do campo (nome, email)
                // getDefaultMessage = mensagem do DTO
                .forEach(error -> {
                    erros.put(error.getField(), error.getDefaultMessage());
                });
        // badRequest = Status 400
        return ResponseEntity.badRequest()
                .body(erros);
    }
}