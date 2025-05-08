package fr.globalmarket.domain.port.out;

import fr.globalmarket.domain.core.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

}