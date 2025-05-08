package fr.globalmarket.adapter.outbound.persistence.adapter;

import fr.globalmarket.adapter.exception.RepositoryOperationException;
import fr.globalmarket.adapter.outbound.persistence.entity.AssetEntity;
import fr.globalmarket.adapter.outbound.persistence.entity.PriceHistoryEntity;
import fr.globalmarket.adapter.outbound.persistence.mapper.PriceHistoryMapper;
import fr.globalmarket.adapter.outbound.persistence.repository.PriceHistoryRepository;
import fr.globalmarket.domain.core.model.Asset;
import fr.globalmarket.domain.core.model.AssetType;
import fr.globalmarket.domain.core.model.PriceHistory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static fr.globalmarket.util.DataConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceHistoryRepositoryAdapterTest {

    @Mock
    private PriceHistoryRepository priceHistoryRepository;

    @Mock
    private PriceHistoryMapper priceHistoryMapper;

    @InjectMocks
    private PriceHistoryRepositoryAdapter priceHistoryRepositoryAdapter;

    private AssetEntity assetEntity;
    private Asset asset;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        assetEntity = AssetEntity.builder()
                .id(10L)
                .name(MICROSOFT_ASSET_NAME)
                .type(AssetType.STOCK)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        asset = Asset.builder()
                .id(10L)
                .name(MICROSOFT_ASSET_NAME)
                .type(AssetType.STOCK)
                .price(MICROSOFT_ASSET_PRICE_1)
                .build();

        now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }


    @Test
    @DisplayName("Should return PriceHistory when repository finds entity")
    void shouldReturnPriceHistoryWhenRepositoryFindsEntity() {
        Long searchId = 1L;
        PriceHistoryEntity foundEntity = PriceHistoryEntity.builder()
                .id(searchId)
                .asset(assetEntity)
                .price(MICROSOFT_ASSET_PRICE_2)
                .timestamp(now)
                .build();

        PriceHistory expectedPriceHistory = PriceHistory.builder()
                .id(searchId)
                .asset(asset)
                .price(MICROSOFT_ASSET_PRICE_2)
                .timestamp(now)
                .build();

        when(priceHistoryRepository.findById(searchId)).thenReturn(Optional.of(foundEntity));
        when(priceHistoryMapper.toDomain(foundEntity)).thenReturn(expectedPriceHistory);

        Optional<PriceHistory> optFound = priceHistoryRepositoryAdapter.findById(searchId);

        assertThat(optFound).isPresent();
        PriceHistory found = optFound.get();
        assertThat(found).isNotNull();
        assertThat(found).isSameAs(expectedPriceHistory);

        verify(priceHistoryRepository, times(1)).findById(searchId);
        verify(priceHistoryMapper, times(1)).toDomain(foundEntity);
    }

    @Test
    @DisplayName("Should return empty Optional when repository finds nothing")
    void shouldReturnEmptyWhenRepositoryFindsNothing() {
        Long searchId = 2L;

        when(priceHistoryRepository.findById(searchId)).thenReturn(Optional.empty());

        Optional<PriceHistory> optFound = priceHistoryRepositoryAdapter.findById(searchId);
        assertThat(optFound).isEmpty();

        verify(priceHistoryRepository, times(1)).findById(searchId);
        verify(priceHistoryMapper, never()).toDomain(any(PriceHistoryEntity.class));
    }

    @Test
    @DisplayName("Should throw RepositoryOperationException when repository throws DataAccessException")
    void shouldThrowRepositoryOperationExceptionWhenRepositoryThrowsDataAccessException() {
        Long searchId = 3L;
        DataAccessException dbException = new QueryTimeoutException("DB Timeout during PriceHistory fetch");

        when(priceHistoryRepository.findById(searchId)).thenThrow(dbException);

        RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class,
                () -> priceHistoryRepositoryAdapter.findById(searchId)
        );

        assertThat(exception.getCause()).isSameAs(dbException);
        assertThat(exception.getMessage()).contains(String.format("Failed to fetch PriceHistory with id '%s'.", searchId));

        verify(priceHistoryRepository, times(1)).findById(searchId);
        verify(priceHistoryMapper, never()).toDomain(any(PriceHistoryEntity.class));
    }
}