package fr.globalmarket.adapter.outbound.persistence.repository;

import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity buildValidUserEntity(String email, String password, BigDecimal capital) {
        return UserEntity.builder()
                .email(email)
                .passwordHash(password)
                .capital(capital)
                .build();
    }

    private UserEntity buildDefaultUserEntity() {
        return buildValidUserEntity(EMAIL_USER_1, PASSWORD_HASH_USER_1, INITIAL_CAPITAL);
    }

    @Test
    @DisplayName("Should save and find user by ID")
    void shouldSaveAndFindUserById() {
        UserEntity userEntity = buildDefaultUserEntity();

        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);
        entityManager.clear();

        Optional<UserEntity> optUserEntity = userRepository.findById(savedUserEntity.getId());

        assertThat(optUserEntity).isPresent();
        UserEntity foundUserEntity = optUserEntity.get();

        assertThat(foundUserEntity).isNotNull();
        assertThat(foundUserEntity.getEmail()).isEqualTo(EMAIL_USER_1);
        assertThat(foundUserEntity.getPasswordHash()).isEqualTo(PASSWORD_HASH_USER_1);
        assertThat(foundUserEntity.getCapital()).isEqualByComparingTo(INITIAL_CAPITAL);
    }

    @Test
    @DisplayName("Should return empty Optional when finding non-existent User ID")
    void shouldReturnEmptyWhenFindingNonExistentUser() {
        Long nonExistentUserEntityID = 999L;
        Optional<UserEntity> optUserEntity = userRepository.findById(nonExistentUserEntityID);
        assertThat(optUserEntity).isNotPresent();
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when saving user with duplicate email")
    void shouldThrowExceptionWhenSavingUserWithDuplicateEmail() {
        UserEntity userEntity1 = buildDefaultUserEntity();
        UserEntity userEntity2 = buildDefaultUserEntity();

        userRepository.saveAndFlush(userEntity1);
        entityManager.detach(userEntity1);

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(userEntity2)
        );

        assertThat(exception.getMessage()).contains("constraint");

    }

    @Test
    @DisplayName("Should save and delete user")
    void shouldSaveAndDeleteUser() {
        UserEntity userEntity = buildDefaultUserEntity();

        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);
        Long userEntityId = savedUserEntity.getId();
        userRepository.deleteById(userEntityId);
        userRepository.flush();

        Optional<UserEntity> optUserEntity = userRepository.findById(userEntityId);

        assertThat(optUserEntity).isNotPresent();
    }

    @Test
    @DisplayName("Should save and upd   ate user")
    void shouldSaveAndUpdateUser() {
        UserEntity userEntity = buildDefaultUserEntity();

        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);
        Long savedUserEntityId = savedUserEntity.getId();
        entityManager.detach(userEntity);

        UserEntity managedUserEntity = userRepository.findById(savedUserEntityId).orElseThrow();
        managedUserEntity.setEmail(EMAIL_USER_2);
        userRepository.saveAndFlush(managedUserEntity);
        entityManager.detach(managedUserEntity);

        UserEntity updatedUserEntity = userRepository.findById(savedUserEntityId).orElseThrow();
        assertThat(updatedUserEntity.getId()).isEqualTo(savedUserEntityId);
        assertThat(updatedUserEntity.getEmail()).isEqualTo(EMAIL_USER_2);
        assertThat(updatedUserEntity.getPasswordHash()).isEqualTo(PASSWORD_HASH_USER_1);
        assertThat(updatedUserEntity.getCapital()).isEqualByComparingTo(INITIAL_CAPITAL);
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        UserEntity userEntity1 = buildDefaultUserEntity();
        UserEntity userEntity2 = buildValidUserEntity(EMAIL_USER_2, PASSWORD_HASH_USER_1, INITIAL_CAPITAL);

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);
        userRepository.flush();
        entityManager.clear();

        List<UserEntity> userEntities = userRepository.findAll();

        assertThat(userEntities).hasSize(2);
        assertThat(userEntities).extracting(UserEntity::getEmail)
                .containsExactlyInAnyOrder(EMAIL_USER_1, EMAIL_USER_2);
    }

}