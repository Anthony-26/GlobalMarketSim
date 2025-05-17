package fr.globalmarket.application.port.out;

import fr.globalmarket.domain.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryPort {

    Optional<PriceHistory> findById(Long id);

}
