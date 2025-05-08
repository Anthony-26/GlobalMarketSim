package fr.globalmarket.adapter.outbound.persistence.repository;

import fr.globalmarket.adapter.outbound.persistence.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
}
