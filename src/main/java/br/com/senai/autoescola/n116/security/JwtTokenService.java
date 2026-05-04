package br.com.senai.autoescola.n116.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtTokenService {
    private final String issuer = "Auto Escola N116";


    private final Algorithm algorithm;

    public JwtTokenService(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(UserDetails user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    public boolean isTokenValidForLogin(String token, String login) {
        String subject = getSubjectFromToken(token);
        return subject.equals(login) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        var decoded = JWT.decode(token);
        return decoded.getExpiresAt().before(new Date());
    }

    public String getSubjectFromToken(String token) {
        var verifier = JWT.require(algorithm).withIssuer(issuer).build();
        return verifier.verify(token).getSubject();
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}
