package fr.globalmarket.adapter.in.rest;

import fr.globalmarket.adapter.in.dto.RegisterRequestDto;
import fr.globalmarket.adapter.in.dto.UserResponseDto;
import fr.globalmarket.application.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserResponseDto registeredUser = authService.registerUser(registerRequestDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
