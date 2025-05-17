package fr.globalmarket.application.service.auth;

import fr.globalmarket.adapter.in.dto.RegisterRequestDto;
import fr.globalmarket.adapter.in.dto.UserResponseDto;

public interface AuthService {

    UserResponseDto registerUser(RegisterRequestDto request);

}
