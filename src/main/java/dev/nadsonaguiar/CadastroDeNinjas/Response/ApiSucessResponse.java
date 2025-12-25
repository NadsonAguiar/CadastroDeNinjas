package dev.nadsonaguiar.CadastroDeNinjas.Response;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSucessResponse<T> {
    private LocalDateTime timestamp;
    private boolean sucess;
    private String message;
    private T data;

    private ApiSucessResponse(boolean sucess, String message, T data){
        this.timestamp = LocalDateTime.now();
        this.sucess = sucess;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiSucessResponse<T> sucess(T data){
        return new ApiSucessResponse<>(true, null, data);
    }

    public static <T> ApiSucessResponse<T> sucess(String message, T data){
        return new ApiSucessResponse<>(true, message, data);
    }

    public static <T> ApiSucessResponse<T> sucess(String message){
        return new ApiSucessResponse<>(true, message, null);
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSucess() {
        return sucess;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
