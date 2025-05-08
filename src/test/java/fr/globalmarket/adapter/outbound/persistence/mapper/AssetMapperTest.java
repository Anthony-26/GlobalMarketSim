package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.AssetEntity;
import fr.globalmarket.domain.core.model.Asset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetMapperTest {

    private final AssetMapper assetMapper = new AssetMapper();

    @Test
    @DisplayName("Should map AssetEntity to Asset domain object successfully")
    void shouldMapAssetEntityToAssetSuccessfully() {
        AssetEntity assetEntity = AssetEntity.builder()
                .id(1L)
                .name(MICROSOFT_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        Asset asset = assetMapper.toDomain(assetEntity);

        assertThat(asset).isNotNull();
        assertThat(asset.getId()).isEqualTo(1L);
        assertThat(asset.getName()).isEqualTo(MICROSOFT_ASSET_NAME);
        assertThat(asset.getPrice()).isEqualByComparingTo(MICROSOFT_ASSET_PRICE_1);
        assertThat(asset.getType()).isEqualTo(STOCK_ASSET_TYPE);
    }

    @Test
    @DisplayName("Should throw NPE when mapping entity with null name")
    void shouldThrowNpeWhenMappingEntityWithNullName() {
        AssetEntity assetEntity = AssetEntity.builder()
                .id(2L)
                .name(null)
                .type(STOCK_ASSET_TYPE)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> assetMapper.toDomain(assetEntity));

        assertThat(exception.getMessage()).contains("name", "null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when entity parameter is null")
    void shouldThrowNpeWhenEntityIsNull() {
        AssetEntity nullEntity = null;

        assertThrows(NullPointerException.class,
                () -> assetMapper.toDomain(nullEntity));
    }

}
