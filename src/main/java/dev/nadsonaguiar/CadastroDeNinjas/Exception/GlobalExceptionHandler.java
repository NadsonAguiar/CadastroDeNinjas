package dev.nadsonaguiar.CadastroDeNinjas.Exception;

import dev.nadsonaguiar.CadastroDeNinjas.Response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


// Classe criada para tratar exceptions
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Diz que esse metodo trata erro de validação
    @ExceptionHandler(MethodArgumentNotValidException.class )
    public ResponseEntity<ApiErrorResponse> handlerValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        String message =
                // getBindingResult pega o relatório da validação contendo qual campo deu erro, qual anotação falhou, qual mensagem e valor enviado
                ex.getBindingResult()
                // getFieldErrors = lista de campos com erro nos DTO
                .getFieldErrors()
                // get(0) = Pega apenas o primeiro erro
                .get(0)
                // getDefaultMessage = Extrai mensagem escrita no DTO
                .getDefaultMessage();


        ApiErrorResponse error = new ApiErrorResponse(
                // Status
                HttpStatus.BAD_REQUEST.value(),
                //Error
                "Bad Request",
                // Mensagem de erro
                message,
                //Caminho uri do erro
                request.getRequestURI()
        );

        return ResponseEntity.badRequest()
                .body(error);


        // Modo anterior com MAP
        /* Map<String, String> erros = new HashMap<>();
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
                .body(erros); */
    }

    @ExceptionHandler(NinjaNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNinjaNotFound(NinjaNotFoundException ex, HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MissaoNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMissaoNotFound(MissaoNotFoundException ex, HttpServletRequest request){

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);

    }





}