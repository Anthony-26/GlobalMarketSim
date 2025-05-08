package fr.globalmarket.adapter.outbound.persistence.repository;

import fr.globalmarket.adapter.outbound.persistence.entity.PriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryEntity, Long> {
}
