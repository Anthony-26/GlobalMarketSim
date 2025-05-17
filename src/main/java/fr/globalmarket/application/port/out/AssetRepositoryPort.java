package fr.globalmarket.application.port.out;

import fr.globalmarket.domain.model.Asset;

import java.util.Optional;

public interface AssetRepositoryPort {

    Optional<Asset> findById(Long id);

}
