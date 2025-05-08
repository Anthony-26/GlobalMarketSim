package fr.globalmarket.adapter.outbound.persistence.adapter;

import fr.globalmarket.adapter.exception.RepositoryOperationException;
import fr.globalmarket.adapter.outbound.persistence.mapper.AssetMapper;
import fr.globalmarket.adapter.outbound.persistence.repository.AssetRepository;
import fr.globalmarket.domain.core.model.Asset;
import fr.globalmarket.domain.port.out.AssetRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AssetRepositoryAdapter implements AssetRepositoryPort {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

    @Override
    public Optional<Asset> findById(Long id) {
        log.debug("Searching for Asset with id '{}' in database.", id);
        try {
            return assetRepository.findById(id).map(assetMapper::toDomain);
        } catch (DataAccessException e) {
            throw new RepositoryOperationException(String.format("Failed to fetch Asset with id '%s'.", id), e);
        }
    }

}