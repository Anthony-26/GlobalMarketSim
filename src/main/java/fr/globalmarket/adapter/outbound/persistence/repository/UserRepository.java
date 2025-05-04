package fr.globalmarket.adapter.outbound.persistence.repository;

import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
