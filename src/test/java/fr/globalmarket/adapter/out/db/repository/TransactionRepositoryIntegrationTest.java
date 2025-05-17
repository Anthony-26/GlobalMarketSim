package fr.globalmarket.adapter.out.db.repository;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.adapter.out.db.entity.TransactionEntity;
import fr.globalmarket.adapter.out.db.entity.UserEntity;
import fr.globalmarket.domain.model.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
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
class TransactionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private UserEntity persistedUser;
    private AssetEntity persistedAsset;

    private final int quantityExecuted = 20;
    private final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    @BeforeEach
    void setUp() {
        persistedAsset = entityManager.persistFlushFind(AssetEntity.builder()
                .name(ELI_LILLY_ASSET_NAME)
                .type(STOCK_ASSET_TYPE)
                .price(ELI_LILLY_ASSET_PRICE_1)
                .build());

        persistedUser = entityManager.persistFlushFind(UserEntity.builder()
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build());
    }

    private TransactionEntity buildValidTransactionEntity(UserEntity user, AssetEntity asset, OrderType orderType,
                                                          int quantity, BigDecimal price, LocalDateTime timestamp) {
        return TransactionEntity.builder()
                .user(user)
                .asset(asset)
                .orderType(orderType)
                .quantity(quantity)
                .price(price)
                .timestamp(timestamp)
                .build();
    }

    private TransactionEntity buildDefaultTransaction() {
        return buildValidTransactionEntity(persistedUser, persistedAsset, BUY_ORDER,
                quantityExecuted, ELI_LILLY_ASSET_PRICE_1, now);
    }

    @Test
    @DisplayName("Should save and find transaction by ID")
    void shouldSaveAndFindTransactionById() {
        TransactionEntity transactionToSave = buildDefaultTransaction();

        TransactionEntity savedTransaction = transactionRepository.saveAndFlush(transactionToSave);
        entityManager.clear();

        Optional<TransactionEntity> optTransaction = transactionRepository.findById(savedTransaction.getId());

        assertThat(optTransaction).isPresent();
        TransactionEntity foundTransaction = optTransaction.get();

        assertThat(foundTransaction.getUser().getId()).isEqualTo(persistedUser.getId());
        assertThat(foundTransaction.getAsset().getId()).isEqualTo(persistedAsset.getId());
        assertThat(foundTransaction.getOrderType()).isEqualTo(BUY_ORDER);
        assertThat(foundTransaction.getQuantity()).isEqualTo(quantityExecuted);
        assertThat(foundTransaction.getPrice()).isEqualByComparingTo(ELI_LILLY_ASSET_PRICE_1);
    }

    @Test
    @DisplayName("Should find all transactions")
    void shouldFindAllTransactions() {
        TransactionEntity transactionToSave1 = buildDefaultTransaction();
        TransactionEntity transactionToSave2 = buildValidTransactionEntity(persistedUser, persistedAsset,
                SELL_ORDER, quantityExecuted, ELI_LILLY_ASSET_PRICE_2,
                LocalDateTime.now().minusDays(45L).truncatedTo(ChronoUnit.MILLIS));

        transactionRepository.save(transactionToSave1);
        transactionRepository.save(transactionToSave2);
        transactionRepository.flush();
        entityManager.clear();

        List<TransactionEntity> foundTransactions = transactionRepository.findAll();

        List<TransactionEntity> selectedTransaction = foundTransactions.stream()
                .filter(transaction -> transaction.getUser().getId().equals(persistedUser.getId()))
                .toList();

        assertThat(selectedTransaction).hasSize(2);
        assertThat(selectedTransaction).extracting(TransactionEntity::getOrderType)
                .containsExactlyInAnyOrder(SELL_ORDER, BUY_ORDER);
    }


    @Test
    @DisplayName("Should return empty Optional when finding non-existent Transaction ID")
    void shouldReturnEmptyWhenFindingNonExistentTransaction() {
        Long nonExistentId = 999L;
        Optional<TransactionEntity> foundOpt = transactionRepository.findById(nonExistentId);
        assertThat(foundOpt).isNotPresent();
    }

    @Test
    @DisplayName("Should throw exception when saving transaction with non-existent user")
    void shouldThrowExceptionWhenSavingTransactionWithNonExistentUser() {
        UserEntity nonPersistedUser = UserEntity.builder()
                .email(EMAIL_USER_1)
                .passwordHash(PASSWORD_HASH_USER_1)
                .capital(INITIAL_CAPITAL)
                .build();

        TransactionEntity transactionToSave = buildValidTransactionEntity(nonPersistedUser, persistedAsset, BUY_ORDER,
                quantityExecuted, ELI_LILLY_ASSET_PRICE_1, now);

        InvalidDataAccessApiUsageException exception = assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> transactionRepository.saveAndFlush(transactionToSave)
        );

        assertThat(exception.getMessage())
                .contains("Not-null property references a transient value", "TransactionEntity.user");
    }

    @Test
    @DisplayName("Should throw exception when saving transaction with non-existent asset ID")
    void shouldThrowExceptionWhenSavingTransactionWithNonExistentAssetId() {
        AssetEntity nonExistentAsset = AssetEntity.builder()
                .id(50L)
                .name(MICROSOFT_ASSET_NAME)
                .price(MICROSOFT_ASSET_PRICE_1)
                .type(STOCK_ASSET_TYPE)
                .build();

        TransactionEntity transactionToSave = buildValidTransactionEntity(persistedUser, nonExistentAsset,
                SELL_ORDER, quantityExecuted, MICROSOFT_ASSET_PRICE_1, now);

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> transactionRepository.saveAndFlush(transactionToSave)
        );
        assertThat(exception.getMessage()).contains("constraint violation", "FOREIGN KEY");
    }
}