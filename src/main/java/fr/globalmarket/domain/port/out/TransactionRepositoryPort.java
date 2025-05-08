package fr.globalmarket.domain.port.out;

import fr.globalmarket.domain.core.model.Transaction;

import java.util.Optional;

public interface TransactionRepositoryPort {

    Optional<Transaction> findById(Long id);

}
