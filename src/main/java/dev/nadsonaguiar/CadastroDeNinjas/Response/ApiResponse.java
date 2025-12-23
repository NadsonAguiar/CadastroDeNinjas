package dev.nadsonaguiar.CadastroDeNinjas.Response;


import java.time.LocalDateTime;

public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private T data;

    public ApiResponse(int status, T data){
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
