package fr.globalmarket.adapter.outbound.persistence.adapter;

import fr.globalmarket.adapter.exception.RepositoryOperationException;
import fr.globalmarket.adapter.outbound.persistence.mapper.UserMapper;
import fr.globalmarket.adapter.outbound.persistence.repository.UserRepository;
import fr.globalmarket.domain.core.model.User;
import fr.globalmarket.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        try {
            log.debug("Searching for User with id '{}' in database.", id);
            return userRepository.findById(id).map(userMapper::toDomain);
        } catch (DataAccessException e) {
            throw new RepositoryOperationException(String.format("Failed to fetch User with id '%s'.", id), e);
        }
    }

}
