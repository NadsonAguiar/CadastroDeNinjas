package dev.nadsonaguiar.CadastroDeNinjas.Ninjas;

import dev.nadsonaguiar.CadastroDeNinjas.Infra.Security.TokenService;
import dev.nadsonaguiar.CadastroDeNinjas.User.*;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.registry.Registry;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
            var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = authenticationManager.authenticate(userNamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));



    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegistryDTO data){
        if(this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptePassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptePassword, data.role());

        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
