package fr.globalmarket.adapter.config;

import fr.globalmarket.adapter.outbound.persistence.adapter.*;
import fr.globalmarket.adapter.outbound.persistence.mapper.*;
import fr.globalmarket.adapter.outbound.persistence.repository.*;
import fr.globalmarket.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    public AssetMapper getMapper() {
        return new AssetMapper();
    }

    @Bean
    public UserMapper createUserMapper() {
        return new UserMapper();
    }

    @Bean
    public PriceHistoryMapper createPriceHistoryMapper(AssetMapper assetMapper) {
        return new PriceHistoryMapper(assetMapper);
    }

    @Bean
    public TransactionMapper createTransactionMapper(AssetMapper assetMapper, UserMapper userMapper) {
        return new TransactionMapper(assetMapper, userMapper);
    }

    @Bean
    public AssetRepositoryPort createAssetRepositoryAdapter(AssetRepository assetRepository,
                                                            AssetMapper assetMapper) {
        return new AssetRepositoryAdapter(assetRepository, assetMapper);
    }

    @Bean
    public UserRepositoryPort createUserRepositoryAdapter(UserRepository userRepository,
                                                          UserMapper userMapper) {
        return new UserRepositoryAdapter(userRepository, userMapper);
    }

    @Bean
    public PriceHistoryPort createPriceHistoryAdapter(PriceHistoryRepository priceHistoryRepository,
                                                      PriceHistoryMapper priceHistoryMapper) {
        return new PriceHistoryRepositoryAdapter(priceHistoryRepository, priceHistoryMapper);
    }

    @Bean
    public TransactionRepositoryPort createTransactionRepositoryAdapter(TransactionRepository transactionRepository,
                                                                        TransactionMapper transactionMapper) {
        return new TransactionRepositoryAdapter(transactionRepository, transactionMapper);
    }

}
