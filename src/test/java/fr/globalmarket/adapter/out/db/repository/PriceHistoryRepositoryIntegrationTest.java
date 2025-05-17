package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.adapter.out.db.entity.PriceHistoryEntity;
import fr.globalmarket.domain.model.AssetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class PriceHistoryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    private AssetEntity persistedAsset;

    @BeforeEach
    void setUp() {
        persistedAsset = entityManager.persistFlushFind(
                AssetEntity.builder()
                        .name(BITCOIN_ASSET_NAME)
                        .type(CRYPTO_ASSET_TYPE)
                        .price(BITCOIN_PRICE_1)
                        .build()
        );
    }

    private PriceHistoryEntity buildPriceHistoryEntity(AssetEntity asset, BigDecimal price, LocalDateTime timestamp) {
        return PriceHistoryEntity.builder()
                .asset(asset)
                .price(price)
                .timestamp(timestamp)
                .build();
    }

    private PriceHistoryEntity buildDefaultPriceHistoryEntity() {
        return buildPriceHistoryEntity(persistedAsset, BITCOIN_PRICE_1, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @DisplayName("Should save and find price history by ID")
        void shouldSaveAndFindPriceHistoryById() {
            PriceHistoryEntity priceHistoryToSave = buildDefaultPriceHistoryEntity();

            PriceHistoryEntity savedPriceHistory = priceHistoryRepository.saveAndFlush(priceHistoryToSave);
            entityManager.clear();

            Optional<PriceHistoryEntity> foundOpt = priceHistoryRepository.findById(savedPriceHistory.getId());

            assertThat(foundOpt).isPresent();
            PriceHistoryEntity found = foundOpt.get();

            assertThat(found.getId()).isNotNull();
            assertThat(found.getPrice()).isEqualByComparingTo(BITCOIN_PRICE_1);
            assertThat(found.getTimestamp()).isEqualTo(priceHistoryToSave.getTimestamp());
            assertThat(found.getAsset()).isNotNull();
            assertThat(found.getAsset().getId()).isEqualTo(persistedAsset.getId());
    }

    @Test
    @DisplayName("Should save and update price history price")
    void shouldSaveAndUpdatePriceHistoryPrice() {
        PriceHistoryEntity priceHistoryToSave = buildDefaultPriceHistoryEntity();
        PriceHistoryEntity savedPriceHistory = priceHistoryRepository.saveAndFlush(priceHistoryToSave);
        Long historyId = savedPriceHistory.getId();
        entityManager.clear();

        PriceHistoryEntity managedHistory = priceHistoryRepository.findById(historyId).orElseThrow();
        managedHistory.setPrice(BITCOIN_PRICE_2);

        priceHistoryRepository.saveAndFlush(managedHistory);
        entityManager.clear();

        PriceHistoryEntity updatedHistory = priceHistoryRepository.findById(historyId).orElseThrow();

        assertThat(updatedHistory.getPrice()).isEqualByComparingTo(BITCOIN_PRICE_2);
        assertThat(updatedHistory.getTimestamp()).isEqualTo(priceHistoryToSave.getTimestamp());
        assertThat(updatedHistory.getAsset().getId()).isEqualTo(persistedAsset.getId());
    }

    @Test
    @DisplayName("Should save and delete price history")
    void shouldSaveAndDeletePriceHistory() {
        PriceHistoryEntity priceHistoryToSave = buildDefaultPriceHistoryEntity();
        PriceHistoryEntity savedPriceHistory = priceHistoryRepository.saveAndFlush(priceHistoryToSave);
        Long historyId = savedPriceHistory.getId();

        priceHistoryRepository.deleteById(historyId);
        priceHistoryRepository.flush();

        Optional<PriceHistoryEntity> foundOpt = priceHistoryRepository.findById(historyId);

        assertThat(foundOpt).isNotPresent();
    }

    @Test
    @DisplayName("Should find all price histories for the asset")
    void shouldFindAllPriceHistories() {
        LocalDateTime time1 = LocalDateTime.now().minusMinutes(1L).truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime time2 = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        PriceHistoryEntity history1 = buildPriceHistoryEntity(persistedAsset, BITCOIN_PRICE_1, time1);
        PriceHistoryEntity history2 = buildPriceHistoryEntity(persistedAsset, BITCOIN_PRICE_2, time2);

        priceHistoryRepository.save(history1);
        priceHistoryRepository.save(history2);
        priceHistoryRepository.flush();
        entityManager.clear();

        List<PriceHistoryEntity> allHistories = priceHistoryRepository.findAll();

        List<PriceHistoryEntity> assetHistories = allHistories.stream()
                .filter(h -> h.getAsset().getId().equals(persistedAsset.getId()))
                .toList();

        assertThat(assetHistories).hasSize(2);
        assertThat(assetHistories).extracting(PriceHistoryEntity::getPrice)
                .containsExactlyInAnyOrder(BITCOIN_PRICE_1, BITCOIN_PRICE_2);
        assertThat(assetHistories).extracting(PriceHistoryEntity::getTimestamp)
                .containsExactlyInAnyOrder(time1, time2);
    }


    @Test
    @DisplayName("Should return empty Optional when finding non-existent PriceHistory ID")
    void shouldReturnEmptyWhenFindingNonExistentPriceHistory() {
        Long nonExistentId = 999L;
        Optional<PriceHistoryEntity> foundOpt = priceHistoryRepository.findById(nonExistentId);
        assertThat(foundOpt).isNotPresent();
    }

    @Test
    @DisplayName("Should throw Exception when saving history with non-existent Asset FK")
    void shouldThrowExceptionWhenSavingHistoryWithNonExistentAsset() {
        AssetEntity nonPersistedAssetReference = AssetEntity.builder()
                .name(ELI_LILLY_ASSET_NAME)
                .type(AssetType.STOCK)
                .price(ELI_LILLY_ASSET_PRICE_1)
                .build();

        PriceHistoryEntity priceHistoryToSave = buildPriceHistoryEntity(nonPersistedAssetReference, ELI_LILLY_ASSET_PRICE_1, LocalDateTime.now());

        InvalidDataAccessApiUsageException exception = assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> priceHistoryRepository.saveAndFlush(priceHistoryToSave)
        );
        assertThat(exception.getMessage())
                .contains("Not-null property references a transient value", "PriceHistoryEntity.asset");
    }
}