package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.PriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryEntity, Long> {
}
