package fr.globalmarket.adapter.outbound.persistence.mapper;

import fr.globalmarket.adapter.outbound.persistence.entity.AssetEntity;
import fr.globalmarket.domain.core.model.Asset;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssetMapper implements Mapper<AssetEntity, Asset> {

    @Override
    public Asset toDomain(@NonNull AssetEntity entity) {
        log.debug("Mapping 'Asset Entity' with id '{}' to 'Asset' domain object.", entity.getId());
        return Asset.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .price(entity.getPrice())
                .build();
    }

}
