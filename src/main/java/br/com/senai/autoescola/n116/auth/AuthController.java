package br.com.senai.autoescola.n116.auth;

import br.com.senai.autoescola.n116.auth.login.AuthLoginHandler;
import br.com.senai.autoescola.n116.auth.login.AuthLoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthLoginHandler authLoginHandler;

    public AuthController(AuthLoginHandler authLoginHandler) {
        this.authLoginHandler = authLoginHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequest req) {
        var response = authLoginHandler.handle(req);
        return ResponseEntity.ok(response);
    }
}
