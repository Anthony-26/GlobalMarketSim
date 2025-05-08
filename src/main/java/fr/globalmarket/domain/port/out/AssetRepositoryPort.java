package fr.globalmarket.domain.port.out;

import fr.globalmarket.domain.core.model.Asset;

import java.util.Optional;

public interface AssetRepositoryPort {

    Optional<Asset> findById(Long id);

}
