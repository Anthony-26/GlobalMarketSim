package fr.globalmarket.adapter.out.db.mapper;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.adapter.out.db.entity.PriceHistoryEntity;
import fr.globalmarket.domain.model.Asset;
import fr.globalmarket.domain.model.PriceHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceHistoryMapperTest {

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private PriceHistoryMapper priceHistoryMapper;

    private AssetEntity assetEntity;
    private Asset asset;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        assetEntity = AssetEntity.builder()
                .id(0L)
                .name(MICROSOFT_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        asset = Asset.builder()
                .id(0L)
                .name(MICROSOFT_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

    @Test
    @DisplayName("Should map PriceHistoryEntity to Asset domain object successfully")
    void shouldMapPriceHistoryEntityToPriceHistorySuccessfully() {
        PriceHistoryEntity priceHistoryEntity = PriceHistoryEntity.builder()
                .id(1L)
                .asset(assetEntity)
                .price(MICROSOFT_ASSET_PRICE_2)
                .timestamp(now)
                .build();

        when(assetMapper.toDomain(assetEntity)).thenReturn(asset);

        PriceHistory priceHistory = priceHistoryMapper.toDomain(priceHistoryEntity);

        assertThat(priceHistory).isNotNull();
        assertThat(priceHistory.getId()).isEqualTo(1L);
        assertThat(priceHistory.getAsset()).isSameAs(asset);
        assertThat(priceHistory.getPrice()).isEqualByComparingTo(MICROSOFT_ASSET_PRICE_2);
        assertThat(priceHistory.getTimestamp()).isEqualTo(now);

        verify(assetMapper, times(1)).toDomain(assetEntity);
    }

    @Test
    @DisplayName("Should throw NPE when mapping entity with null timestamp")
    void shouldThrowNpeWhenMappingEntityWithNullTimestamp() {
        PriceHistoryEntity priceHistoryEntity = PriceHistoryEntity.builder()
                .id(1L)
                .asset(assetEntity)
                .price(MICROSOFT_ASSET_PRICE_2)
                .timestamp(null)
                .build();

        when(assetMapper.toDomain(assetEntity)).thenReturn(asset);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> priceHistoryMapper.toDomain(priceHistoryEntity));

        assertThat(exception.getMessage()).contains("timestamp", "null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when entity parameter is null")
    void shouldThrowNpeWhenEntityIsNull() {
        PriceHistoryEntity priceHistoryEntity = null;

        assertThrows(NullPointerException.class,
                () -> priceHistoryMapper.toDomain(priceHistoryEntity));
    }
}
