package br.com.senai.autoescola.n116.users;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @NonNull
    public UserDetails loadUserByUsername(@NonNull String login) throws UsernameNotFoundException {
        return usersRepository
                .findByLogin(login)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails toUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
