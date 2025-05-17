package fr.globalmarket.adapter.out.db.mapper;

import fr.globalmarket.adapter.out.db.entity.UserEntity;
import fr.globalmarket.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    @DisplayName("Should map UserEntity to User domain object successfully")
    void shouldMapUserEntityToUserSuccessfully() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        User user = userMapper.toDomain(userEntity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo(EMAIL_USER_1);
        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD_HASH_USER_1);
        assertThat(user.getCapital()).isEqualByComparingTo(INITIAL_CAPITAL);
    }

    @Test
    @DisplayName("Should throw NPE when mapping entity with null email")
    void shouldThrowNpeWhenMappingEntityWithNullEmail() {
        UserEntity userEntityWithNullEmail = UserEntity.builder()
                .id(2L)
                .email(null)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> userMapper.toDomain(userEntityWithNullEmail));

        assertThat(exception.getMessage()).contains("email", "null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when entity parameter is null")
    void shouldThrowNpeWhenEntityIsNull() {
        UserEntity nullEntity = null;

        assertThrows(NullPointerException.class,
                () -> userMapper.toDomain(nullEntity));
    }
}