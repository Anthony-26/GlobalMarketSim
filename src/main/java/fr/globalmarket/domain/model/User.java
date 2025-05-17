package fr.globalmarket.domain.model;

import jakarta.validation.constraints.NotNull;
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
    private String passwordHash;

    @NonNull
    private BigDecimal capital;

    @NotNull
    private boolean isEnabled;

}