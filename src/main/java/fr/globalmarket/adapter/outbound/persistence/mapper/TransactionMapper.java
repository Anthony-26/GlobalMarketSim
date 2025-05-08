package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.TransactionEntity;
import fr.globalmarket.domain.core.model.Transaction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TransactionMapper implements Mapper<TransactionEntity, Transaction> {

    private final AssetMapper assetMapper;
    private final UserMapper userMapper;

    @Override
    public Transaction toDomain(@NonNull TransactionEntity entity) {
        log.debug("Mapping 'TransactionEntity' object with id '{}' to 'Transaction' domain object.",
                entity.getId());
        return Transaction.builder()
                .id(entity.getId())
                .user(userMapper.toDomain(entity.getUser()))
                .asset(assetMapper.toDomain(entity.getAsset()))
                .orderType(entity.getOrderType())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .timestamp(entity.getTimestamp())
                .build();
    }

}
