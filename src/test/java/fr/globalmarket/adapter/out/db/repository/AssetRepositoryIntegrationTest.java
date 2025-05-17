package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.domain.model.AssetType;
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
class AssetRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AssetRepository assetRepository;

    private AssetEntity buildValidAssetEntity(String name, AssetType type, BigDecimal price) {
        return AssetEntity.builder()
                .name(name)
                .type(type)
                .price(price)
                .build();
    }

    private AssetEntity buildDefaultAssetEntity() {
        return buildValidAssetEntity(MICROSOFT_ASSET_NAME, STOCK_ASSET_TYPE, MICROSOFT_ASSET_PRICE_1);
    }

    @Test
    @DisplayName("Should save and find asset by ID")
    void shouldSaveAndFindAssetById() {
        AssetEntity assetEntity = buildDefaultAssetEntity();

        AssetEntity savedAssetEntity = assetRepository.saveAndFlush(assetEntity);
        entityManager.detach(assetEntity);

        Optional<AssetEntity> optAssetEntity = assetRepository.findById(savedAssetEntity.getId());

        assertThat(optAssetEntity).isPresent();
        AssetEntity foundAssetEntity = optAssetEntity.get();

        assertThat(foundAssetEntity.getId()).isNotNull();
        assertThat(foundAssetEntity.getName()).isEqualTo(MICROSOFT_ASSET_NAME);
        assertThat(foundAssetEntity.getType()).isEqualTo(STOCK_ASSET_TYPE);
        assertThat(foundAssetEntity.getPrice()).isEqualByComparingTo(MICROSOFT_ASSET_PRICE_1);
    }

    @Test
    @DisplayName("Should return empty Optional when finding non-existent Asset ID")
    void shouldReturnEmptyWhenFindingNonExistentAsset() {
        Long nonExistentAssetID = 999L;
        Optional<AssetEntity> optAssetEntity = assetRepository.findById(nonExistentAssetID);
        assertThat(optAssetEntity).isNotPresent();
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when saving asset with duplicate name")
    void shouldThrowExceptionWhenSavingAssetWithDuplicateName() {
        AssetEntity asset1 = buildDefaultAssetEntity();
        AssetEntity asset2 = buildDefaultAssetEntity();

        assetRepository.saveAndFlush(asset1);
        entityManager.detach(asset1);

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> assetRepository.saveAndFlush(asset2)
        );

        assertThat(exception.getMessage()).contains("constraint");
    }

    @Test
    @DisplayName("Should save and delete asset")
    void shouldSaveAndDeleteAsset() {
        AssetEntity assetEntity = buildDefaultAssetEntity();

        AssetEntity savedAssetEntity = assetRepository.saveAndFlush(assetEntity);
        Long assetEntityId = savedAssetEntity.getId();

        assetRepository.deleteById(assetEntityId);
        entityManager.flush();

        Optional<AssetEntity> optAssetEntity = assetRepository.findById(assetEntityId);

        assertThat(optAssetEntity).isNotPresent();
    }

    @Test
    @DisplayName("Should save and update asset price")
    void shouldSaveAndUpdateAssetPrice() {
        AssetEntity assetEntity =  buildDefaultAssetEntity();

        AssetEntity savedAssetEntity = assetRepository.saveAndFlush(assetEntity);
        Long assetEntityId = savedAssetEntity.getId();
        entityManager.detach(assetEntity);

        AssetEntity managedAssetEntity = assetRepository.findById(assetEntityId).orElseThrow();
        managedAssetEntity.setPrice(MICROSOFT_ASSET_PRICE_2);
        assetRepository.saveAndFlush(managedAssetEntity);
        entityManager.detach(managedAssetEntity);

        AssetEntity updatedAsset = assetRepository.findById(assetEntityId).orElseThrow();
        assertThat(updatedAsset.getPrice()).isEqualByComparingTo(MICROSOFT_ASSET_PRICE_2);
        assertThat(updatedAsset.getName()).isEqualTo(MICROSOFT_ASSET_NAME);
        assertThat(updatedAsset.getType()).isEqualTo(STOCK_ASSET_TYPE);
        assertThat(updatedAsset.getId()).isEqualTo(assetEntityId);
    }

    @Test
    @DisplayName("Should find all saved assets")
    void shouldFindAllAssets() {
        AssetEntity asset1 = buildDefaultAssetEntity();
        AssetEntity asset2 = buildValidAssetEntity(ELI_LILLY_ASSET_NAME, STOCK_ASSET_TYPE, ELI_LILLY_ASSET_PRICE_1);

        assetRepository.save(asset1);
        assetRepository.save(asset2);
        assetRepository.flush();
        entityManager.clear();

        List<AssetEntity> allAssets = assetRepository.findAll();

        assertThat(allAssets).hasSize(2);
        assertThat(allAssets).extracting(AssetEntity::getName)
                .containsExactlyInAnyOrder(MICROSOFT_ASSET_NAME, ELI_LILLY_ASSET_NAME);
    }
}