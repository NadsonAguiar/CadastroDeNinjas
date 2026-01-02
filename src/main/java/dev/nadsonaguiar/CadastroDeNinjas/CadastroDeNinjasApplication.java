package dev.nadsonaguiar.CadastroDeNinjas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CadastroDeNinjasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroDeNinjasApplication.class, args);
	}

}
