package fr.globalmarket.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
@Builder
public class Asset {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private AssetType type;

    @NonNull
    private BigDecimal price;

}
