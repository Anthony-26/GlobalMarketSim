package fr.globalmarket.adapter.outbound.persistence.adapter;

import fr.globalmarket.adapter.exception.RepositoryOperationException;
import fr.globalmarket.adapter.outbound.persistence.mapper.TransactionMapper;
import fr.globalmarket.adapter.outbound.persistence.repository.TransactionRepository;
import fr.globalmarket.domain.core.model.Transaction;
import fr.globalmarket.domain.port.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Optional<Transaction> findById(Long id) {
        try {
            log.debug("Searching for Transaction with id '{}' in database.", id);
            return transactionRepository.findById(id).map(transactionMapper::toDomain);
        } catch (DataAccessException e) {
            throw new RepositoryOperationException(String.format("Failed to fetch Transaction with id '%s'.", id), e);
        }
    }

}
