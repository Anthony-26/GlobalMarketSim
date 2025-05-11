package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import fr.globalmarket.domain.core.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper implements Mapper<UserEntity, User> {

    public User toDomain(UserEntity entity) {
        log.debug("Mapping 'UserEntity' object with id '{}' to 'User' domain object.", entity.getId());
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPasswordHash())
                .capital(entity.getCapital())
                .build();

    }

}
