package fr.globalmarket.application.port.out;

import fr.globalmarket.domain.model.Transaction;

import java.util.Optional;

public interface TransactionRepositoryPort {

    Optional<Transaction> findById(Long id);

}
