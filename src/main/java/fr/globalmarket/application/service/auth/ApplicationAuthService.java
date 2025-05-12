package fr.globalmarket.application.service.auth;

import fr.globalmarket.adapter.inbound.dto.RegisterRequestDto;
import fr.globalmarket.adapter.inbound.dto.UserResponseDto;
import fr.globalmarket.application.exception.EmailAlreadyExistsException;
import fr.globalmarket.application.util.LogSanitizer;
import fr.globalmarket.domain.core.model.User;
import fr.globalmarket.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
public class ApplicationAuthService implements AuthService{

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    private final static BigDecimal INITIAL_CAPITAL = new BigDecimal("100000.00");

    @Override
    @Transactional
    public UserResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        log.info("Registering process started for email '{}'.", LogSanitizer.santize(registerRequestDto.getEmail()));
        userRepositoryPort.findByEmail(registerRequestDto.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException("User already exists.");
        });

        User savedUser = userRepositoryPort.save(User.builder()
                        .email(registerRequestDto.getEmail())
                        .passwordHash(passwordEncoder.encode(registerRequestDto.getPassword()))
                        .capital(INITIAL_CAPITAL)
                        .isEnabled(true)
                .build());

        return UserResponseDto.builder()
                .email(savedUser.getEmail())
                .build();
    }
}
