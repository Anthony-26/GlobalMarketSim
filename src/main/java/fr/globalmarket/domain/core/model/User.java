package fr.globalmarket.domain.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
@Builder
public class User {

    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private BigDecimal capital;

}