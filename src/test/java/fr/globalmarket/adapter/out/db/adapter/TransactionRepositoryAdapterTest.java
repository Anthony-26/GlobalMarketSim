package fr.globalmarket.adapter.out.db.adapter;

import fr.globalmarket.application.exception.RepositoryOperationException;
import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.adapter.out.db.entity.TransactionEntity;
import fr.globalmarket.adapter.out.db.entity.UserEntity;
import fr.globalmarket.adapter.out.db.mapper.TransactionMapper;
import fr.globalmarket.adapter.out.db.repository.TransactionRepository;
import fr.globalmarket.domain.model.Asset;
import fr.globalmarket.domain.model.Transaction;
import fr.globalmarket.domain.model.User;
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
class TransactionRepositoryAdapterTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionRepositoryAdapter transactionRepositoryAdapter;

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();
    }

    private AssetEntity createAssetEntity() {
        return AssetEntity.builder()
                .id(10L)
                .name(BITCOIN_ASSET_NAME)
                .type(CRYPTO_ASSET_TYPE)
                .price(BITCOIN_PRICE_1)
                .build();
    }

    private User createUserDomain() {
        return User.builder()
                .id(1L)
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();
    }

    private Asset createTestAssetDomain() {
        return Asset.builder()
                .id(10L)
                .name(BITCOIN_ASSET_NAME)
                .type(CRYPTO_ASSET_TYPE)
                .price(BITCOIN_PRICE_1)
                .build();
    }

    @Test
    @DisplayName("Should return Transaction when repository finds entity")
    void shouldReturnTransactionWhenRepositoryFindsEntity() {
        Long searchId = 100L;
        int quantity = 10;
        UserEntity userEntity = createUserEntity();
        AssetEntity assetEntity = createAssetEntity();
        User userDomain = createUserDomain();
        Asset assetDomain = createTestAssetDomain();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        TransactionEntity foundEntity = TransactionEntity.builder()
                .id(searchId)
                .user(userEntity)
                .asset(assetEntity)
                .orderType(BUY_ORDER)
                .quantity(quantity)
                .price(BITCOIN_PRICE_2)
                .timestamp(now)
                .build();

        Transaction expectedTransaction = Transaction.builder()
                .id(searchId)
                .user(userDomain)
                .asset(assetDomain)
                .orderType(BUY_ORDER)
                .quantity(quantity)
                .price(BITCOIN_PRICE_2)
                .timestamp(now)
                .build();

        when(transactionRepository.findById(searchId)).thenReturn(Optional.of(foundEntity));
        when(transactionMapper.toDomain(foundEntity)).thenReturn(expectedTransaction);

        Optional<Transaction> optFound = transactionRepositoryAdapter.findById(searchId);

        assertThat(optFound).isPresent();
        Transaction found = optFound.get();
        assertThat(found).isNotNull();
        assertThat(found).isSameAs(expectedTransaction);

        verify(transactionRepository, times(1)).findById(searchId);
        verify(transactionMapper, times(1)).toDomain(foundEntity);
    }

    @Test
    @DisplayName("Should return empty Optional when repository finds nothing")
    void shouldReturnEmptyWhenRepositoryFindsNothing() {
        Long searchId = 200L;

        when(transactionRepository.findById(searchId)).thenReturn(Optional.empty());

        Optional<Transaction> optFound = transactionRepositoryAdapter.findById(searchId);

        assertThat(optFound).isEmpty();

        verify(transactionRepository, times(1)).findById(searchId);
        verify(transactionMapper, never()).toDomain(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("Should throw RepositoryOperationException when repository throws DataAccessException")
    void shouldThrowRepositoryOperationExceptionWhenRepositoryThrowsDataAccessException() {
        Long searchId = 300L;
        DataAccessException dbException = new QueryTimeoutException("DB Timeout during Transaction fetch");

        when(transactionRepository.findById(searchId)).thenThrow(dbException);

        RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class,
                () -> transactionRepositoryAdapter.findById(searchId)
        );

        assertThat(exception.getCause()).isSameAs(dbException);
        assertThat(exception.getMessage()).contains(String.format("Failed to fetch Transaction with id '%s'.", searchId));

        verify(transactionRepository, times(1)).findById(searchId);
        verify(transactionMapper, never()).toDomain(any(TransactionEntity.class));
    }
}