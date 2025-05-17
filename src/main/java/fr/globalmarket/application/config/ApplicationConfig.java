package fr.globalmarket.application.config;

import fr.globalmarket.application.service.auth.ApplicationAuthService;
import fr.globalmarket.application.service.auth.AuthService;
import fr.globalmarket.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthService createAuthService() {
        return new ApplicationAuthService(userRepositoryPort, passwordEncoder);
    }

}
