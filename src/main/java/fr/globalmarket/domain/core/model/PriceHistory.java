package fr.globalmarket.domain.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PriceHistory {

    private Long id;

    @NonNull
    private Asset asset;

    @NonNull
    private BigDecimal price;

    @NonNull
    private LocalDateTime timestamp;

}
