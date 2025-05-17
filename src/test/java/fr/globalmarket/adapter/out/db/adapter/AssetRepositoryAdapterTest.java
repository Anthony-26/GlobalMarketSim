package fr.globalmarket.adapter.out.db.adapter;

import fr.globalmarket.application.exception.RepositoryOperationException;
import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.adapter.out.db.mapper.AssetMapper;
import fr.globalmarket.adapter.out.db.repository.AssetRepository;
import fr.globalmarket.domain.model.Asset;
import fr.globalmarket.domain.model.AssetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;

import java.util.Optional;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetRepositoryAdapterTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetRepositoryAdapter assetRepositoryAdapter;


    @Test
    @DisplayName("Should return Asset when repository finds entity")
    void shouldReturnAssetWhenRepositoryFindsEntity() {
        Long searchId = 1L;
        AssetEntity foundEntity = AssetEntity.builder()
                .id(searchId)
                .name(ELI_LILLY_ASSET_NAME)
                .type(AssetType.STOCK)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        Asset expectedAsset = Asset.builder()
                .id(searchId)
                .name(MICROSOFT_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        when(assetRepository.findById(searchId)).thenReturn(Optional.of(foundEntity));
        when(assetMapper.toDomain(foundEntity)).thenReturn(expectedAsset);

        Optional<Asset> optFound = assetRepositoryAdapter.findById(searchId);

        assertThat(optFound).isPresent();

        Asset found = optFound.get();
        assertThat(found).isNotNull();
        assertThat(found).isSameAs(expectedAsset);

        verify(assetRepository, times(1)).findById(searchId);
        verify(assetMapper, times(1)).toDomain(foundEntity);
    }

    @Test
    @DisplayName("Should return empty Optional when repository finds nothing")
    void shouldReturnEmptyWhenRepositoryFindsNothing() {
        Long searchId = 2L;

        when(assetRepository.findById(searchId)).thenReturn(Optional.empty());

        Optional<Asset> optFound = assetRepositoryAdapter.findById(searchId);
        assertThat(optFound).isEmpty();

        verify(assetRepository, times(1)).findById(searchId);
        verify(assetMapper, never()).toDomain(any(AssetEntity.class));
    }

    @Test
    @DisplayName("Should throw RepositoryOperationException when repository throws DataAccessException")
    void shouldThrowRepositoryOperationExceptionWhenRepositoryThrowsDataAccessException() {
        Long searchId = 3L;
        DataAccessException dbException = new QueryTimeoutException("Timeout connecting to DB");

        when(assetRepository.findById(searchId)).thenThrow(dbException);

        RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class,
                () -> assetRepositoryAdapter.findById(searchId)
        );

        assertThat(exception.getCause()).isSameAs(dbException);
        assertThat(exception.getMessage()).contains(String.format("Failed to fetch Asset with id '%s'.", searchId));

        verify(assetRepository, times(1)).findById(searchId);
        verify(assetMapper, never()).toDomain(any(AssetEntity.class));
    }

}
