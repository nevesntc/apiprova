package br.com.neves.vassourasecommerce.controller;

import br.com.neves.vassourasecommerce.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final org.springframework.security.provisioning.UserDetailsManager userDetailsManager;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, JwtService jwtService,
                          org.springframework.security.provisioning.UserDetailsManager userDetailsManager,
                          org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    record LoginRequest(String username, String password) {}
    record TokenResponse(String token) {}
    record RegisterRequest(String username, String password) {}
    record MessageResponse(String message) {}

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        Authentication a = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        List<String> roles = a.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String token = jwtService.generateToken(a.getName(), roles);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest req) {
        if (userDetailsManager.userExists(req.username())) {
            return ResponseEntity.status(400).body(new MessageResponse("Usuário já existe"));
        }
        org.springframework.security.core.userdetails.User newUser =
            (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User
                .withUsername(req.username())
                .password(passwordEncoder.encode(req.password()))
                .roles("USER")
                .build();
        userDetailsManager.createUser(newUser);
        return ResponseEntity.status(201).body(new MessageResponse("Usuário criado com sucesso"));
    }
}
