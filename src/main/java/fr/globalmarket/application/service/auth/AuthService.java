package fr.globalmarket.application.service.auth;

import fr.globalmarket.adapter.inbound.dto.RegisterRequestDto;
import fr.globalmarket.adapter.inbound.dto.UserResponseDto;

public interface AuthService {

    UserResponseDto registerUser(RegisterRequestDto request);

}
