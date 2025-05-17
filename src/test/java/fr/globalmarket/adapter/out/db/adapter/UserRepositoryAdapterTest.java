package fr.globalmarket.adapter.out.db.adapter;

import fr.globalmarket.application.exception.RepositoryOperationException;
import fr.globalmarket.adapter.out.db.entity.UserEntity;
import fr.globalmarket.adapter.out.db.mapper.UserMapper;
import fr.globalmarket.adapter.out.db.repository.UserRepository;
import fr.globalmarket.domain.model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;

import java.util.Optional;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    @DisplayName("Should return User when repository finds entity")
    void shouldReturnUserWhenRepositoryFindsEntity() {
        Long searchId = 1L;
        UserEntity foundEntity = UserEntity.builder()
                .id(searchId)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        User expectedUser = User.builder()
                .id(searchId)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        when(userRepository.findById(searchId)).thenReturn(Optional.of(foundEntity));
        when(userMapper.toDomain(foundEntity)).thenReturn(expectedUser);

        Optional<User> optFound = userRepositoryAdapter.findById(searchId);

        assertThat(optFound).isPresent();
        User found = optFound.get();
        assertThat(found).isNotNull();
        assertThat(found).isSameAs(expectedUser);

        verify(userRepository, times(1)).findById(searchId);
        verify(userMapper, times(1)).toDomain(foundEntity);
    }

    @Test
    @DisplayName("Should return empty Optional when repository finds nothing")
    void shouldReturnEmptyWhenRepositoryFindsNothing() {
        Long searchId = 2L;

        when(userRepository.findById(searchId)).thenReturn(Optional.empty());

        Optional<User> optFound = userRepositoryAdapter.findById(searchId);

        assertThat(optFound).isEmpty();

        verify(userRepository, times(1)).findById(searchId);
        verify(userMapper, never()).toDomain(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw RepositoryOperationException when repository throws DataAccessException")
    void shouldThrowRepositoryOperationExceptionWhenRepositoryThrowsDataAccessException() {
        Long searchId = 3L;
        DataAccessException dbException = new RecoverableDataAccessException("DB connection failed");

        when(userRepository.findById(searchId)).thenThrow(dbException);

        RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class,
                () -> userRepositoryAdapter.findById(searchId)
        );

        assertThat(exception.getCause()).isSameAs(dbException);
        assertThat(exception.getMessage()).contains(String.format("Failed to fetch User with id '%s'.", searchId));

        verify(userRepository, times(1)).findById(searchId);
        verify(userMapper, never()).toDomain(any(UserEntity.class));
    }
}