package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
}
