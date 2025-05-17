package fr.globalmarket.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class Transaction {

    private Long id;

    @NonNull
    private User user;

    @NonNull
    private Asset asset;

    @NonNull
    private OrderType orderType;

    private int quantity;

    @NonNull
    private BigDecimal price;

    @NonNull
    private LocalDateTime timestamp;

}
