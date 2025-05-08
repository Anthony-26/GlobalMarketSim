package fr.globalmarket.domain.port.out;

import fr.globalmarket.domain.core.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryPort {

    Optional<PriceHistory> findById(Long id);

}
