package fr.globalmarket.adapter.outbound.persistence.repository;

import fr.globalmarket.adapter.outbound.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
