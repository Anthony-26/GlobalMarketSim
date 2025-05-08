package fr.globalmarket.adapter.outbound.persistence.adapter;

import fr.globalmarket.adapter.exception.RepositoryOperationException;
import fr.globalmarket.adapter.outbound.persistence.mapper.PriceHistoryMapper;
import fr.globalmarket.adapter.outbound.persistence.repository.PriceHistoryRepository;
import fr.globalmarket.domain.core.model.PriceHistory;
import fr.globalmarket.domain.port.out.PriceHistoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PriceHistoryRepositoryAdapter implements PriceHistoryPort {

    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceHistoryMapper priceHistoryMapper;

    @Override
    public Optional<PriceHistory> findById(Long id) {
        try {
            log.debug("Searching for PriceHistory with id '{}' in database.", id);
            return priceHistoryRepository.findById(id).map(priceHistoryMapper::toDomain);
        } catch (DataAccessException e) {
            throw new RepositoryOperationException(String.format("Failed to fetch PriceHistory with id '%s'.", id), e);
        }
    }

}
