package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.AssetEntity;
import fr.globalmarket.adapter.outbound.persistence.entity.TransactionEntity;
import fr.globalmarket.adapter.outbound.persistence.entity.UserEntity;
import fr.globalmarket.domain.core.model.Asset;
import fr.globalmarket.domain.core.model.Transaction;
import fr.globalmarket.domain.core.model.User;
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
class TransactionMapperTest {

    @Mock
    private AssetMapper assetMapper;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TransactionMapper transactionMapper;

    private UserEntity userEntity;
    private AssetEntity assetEntity;
    private User user;
    private Asset asset;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        assetEntity = AssetEntity.builder()
                .id(10L)
                .name(ELI_LILLY_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(ELI_LILLY_ASSET_PRICE_1)
                .build();

        user = User.builder()
                .email(EMAIL_USER_1)
                .password(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        asset = Asset.builder()
                .id(10L)
                .name(ELI_LILLY_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(ELI_LILLY_ASSET_PRICE_1)
                .build();

        now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }


    @Test
    @DisplayName("Should map TransactionEntity to Transaction domain object successfully")
    void shouldMapTransactionEntityToTransactionSuccessfully() {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .id(100L)
                .user(userEntity)
                .asset(assetEntity)
                .orderType(BUY_ORDER)
                .quantity(50)
                .price(ELI_LILLY_ASSET_PRICE_1)
                .timestamp(now)
                .build();

        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(assetMapper.toDomain(assetEntity)).thenReturn(asset);

        Transaction transaction = transactionMapper.toDomain(transactionEntity);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(100L);
        assertThat(transaction.getOrderType()).isEqualTo(BUY_ORDER);
        assertThat(transaction.getQuantity()).isEqualTo(50);
        assertThat(transaction.getPrice()).isEqualByComparingTo(ELI_LILLY_ASSET_PRICE_1);
        assertThat(transaction.getTimestamp()).isEqualTo(now);
        assertThat(transaction.getUser()).isSameAs(user);
        assertThat(transaction.getAsset()).isSameAs(asset);

        verify(userMapper, times(1)).toDomain(userEntity);
        verify(assetMapper, times(1)).toDomain(assetEntity);
    }

    @Test
    @DisplayName("Should throw NPE when mapping entity with null timestamp")
    void shouldThrowNpeWhenMappingEntityWithNullTimestamp() {
        TransactionEntity transactionEntityWithNullTimestamp = TransactionEntity.builder()
                .id(101L)
                .user(userEntity)
                .asset(assetEntity)
                .orderType(SELL_ORDER)
                .quantity(10)
                .price(ELI_LILLY_ASSET_PRICE_2)
                .timestamp(null)
                .build();

        when(userMapper.toDomain(userEntity)).thenReturn(user);
        when(assetMapper.toDomain(assetEntity)).thenReturn(asset);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> transactionMapper.toDomain(transactionEntityWithNullTimestamp));

        assertThat(exception.getMessage()).contains("timestamp", "null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when entity parameter is null")
    void shouldThrowNpeWhenEntityIsNull() {
        TransactionEntity nullEntity = null;

        assertThrows(NullPointerException.class,
                () -> transactionMapper.toDomain(nullEntity));

        verify(userMapper, never()).toDomain(any());
        verify(assetMapper, never()).toDomain(any());
    }

}