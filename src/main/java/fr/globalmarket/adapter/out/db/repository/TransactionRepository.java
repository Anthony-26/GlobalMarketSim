package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
