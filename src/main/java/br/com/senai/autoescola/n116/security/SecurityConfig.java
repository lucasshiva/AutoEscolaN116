package br.com.senai.autoescola.n116.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {
	private final JWTSecurityFilter securityFilter;

	public SecurityConfig(JWTSecurityFilter securityFilter) {
		this.securityFilter = securityFilter;
	}

	@Value("${app.cors.allowed-origins}")
	private String allowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.csrf(AbstractHttpConfigurer::disable)
				   .cors(cors -> cors.configurationSource(request -> {
					   var config = new CorsConfiguration();
					   config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
					   config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
					   config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
					   config.setAllowCredentials(true);
					   return config;
				   }))
				   .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .authorizeHttpRequests(a -> a
						   .requestMatchers("/auth/**")
						   .permitAll()
						   .requestMatchers("/v3/api-docs/**", "/scalar/**", "/swagger-ui.html/**", "/swagger-ui/**")
						   .permitAll()
						   .anyRequest()
						   .authenticated())
				   .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
