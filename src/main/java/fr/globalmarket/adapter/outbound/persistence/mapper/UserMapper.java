package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import fr.globalmarket.application.util.LogSanitizer;
import fr.globalmarket.domain.core.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper implements Mapper<UserEntity, User> {

    public User toDomain(UserEntity entity) {
        log.debug("Mapping 'UserEntity' object with id '{}' to 'User' domain object.", entity.getId());
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .capital(entity.getCapital())
                .isEnabled(entity.isEnabled())
                .build();

    }

    public UserEntity toEntity(User user) {
        log.debug("Mapping 'User' object with email '{}' to 'UserEntity' object.", LogSanitizer.santize(user.getEmail()));
        return UserEntity.builder()
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .capital(user.getCapital())
                .isEnabled(user.isEnabled())
                .build();
    }

}
