package br.com.senai.autoescola.n116.auth;

import br.com.senai.autoescola.n116.auth.login.AuthLoginHandler;
import br.com.senai.autoescola.n116.auth.login.AuthLoginRequest;
import br.com.senai.autoescola.n116.auth.login.AuthLoginResponse;
import br.com.senai.autoescola.n116.auth.register.AuthRegisterHandler;
import br.com.senai.autoescola.n116.auth.register.AuthRegisterRequest;
import br.com.senai.autoescola.n116.auth.register.AuthRegisterResponse;
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
    private final AuthRegisterHandler authRegisterHandler;

    public AuthController(
            AuthLoginHandler authLoginHandler,
            AuthRegisterHandler authRegisterHandler
    ) {
        this.authLoginHandler = authLoginHandler;
        this.authRegisterHandler = authRegisterHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest req) {
        var response = authLoginHandler.handle(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(@RequestBody @Valid AuthRegisterRequest req) {
        var response = authRegisterHandler.handle(req);
        return ResponseEntity.ok(response);
    }
}
