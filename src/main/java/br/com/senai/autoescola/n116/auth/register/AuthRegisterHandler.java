package br.com.senai.autoescola.n116.auth.register;

import br.com.senai.autoescola.n116.auth.UserAlreadyExistsException;
import br.com.senai.autoescola.n116.security.JwtTokenService;
import br.com.senai.autoescola.n116.users.Role;
import br.com.senai.autoescola.n116.users.User;
import br.com.senai.autoescola.n116.users.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthRegisterHandler {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public AuthRegisterHandler(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService tokenService
    ) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public AuthRegisterResponse handle(AuthRegisterRequest req) {
        var login = usersRepository.findByLogin(req.login());
        if (login.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var encoded = passwordEncoder.encode(req.senha());
        var user = new User(null, req.login(), encoded, Role.USER);
        var saved = usersRepository.save(user);
        var token = tokenService.generateToken(saved);
        return new AuthRegisterResponse(token);
    }
}
