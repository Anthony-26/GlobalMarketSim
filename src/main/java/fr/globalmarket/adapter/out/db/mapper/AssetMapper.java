package fr.globalmarket.adapter.out.db.mapper;

import fr.globalmarket.adapter.out.db.entity.AssetEntity;
import fr.globalmarket.domain.model.Asset;
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
