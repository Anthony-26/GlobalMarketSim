package fr.globalmarket.application.port.out;

import fr.globalmarket.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);

}