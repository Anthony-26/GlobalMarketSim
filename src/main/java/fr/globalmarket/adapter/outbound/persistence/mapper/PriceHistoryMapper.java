package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.PriceHistoryEntity;
import fr.globalmarket.domain.core.model.PriceHistory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PriceHistoryMapper implements Mapper<PriceHistoryEntity, PriceHistory> {

    private final AssetMapper assetMapper;

    @Override
    public PriceHistory toDomain(@NonNull PriceHistoryEntity entity) {
        log.debug("Mapping 'PriceHistoryEntity' with id '{}' to 'PriceHistory' domain object.", entity.getId());
        return PriceHistory.builder()
                .id(entity.getId())
                .asset(assetMapper.toDomain(entity.getAsset()))
                .price(entity.getPrice())
                .timestamp(entity.getTimestamp())
                .build();
    }

}
