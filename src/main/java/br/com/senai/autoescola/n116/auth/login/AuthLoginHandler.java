package br.com.senai.autoescola.n116.auth.login;

import br.com.senai.autoescola.n116.security.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthLoginHandler {
    private final AuthenticationManager authManager;
    private final JwtTokenService tokenService;

    public AuthLoginHandler(AuthenticationManager authManager, JwtTokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    public AuthLoginResponse handle(AuthLoginRequest req) {
        var token = new UsernamePasswordAuthenticationToken(req.login(), req.senha());
        var auth = authManager.authenticate(token);
        var jwtToken = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return new AuthLoginResponse(jwtToken);
    }
}
