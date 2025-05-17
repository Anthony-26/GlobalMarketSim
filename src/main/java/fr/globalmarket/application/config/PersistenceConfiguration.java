package fr.globalmarket.application.config;

import fr.globalmarket.adapter.out.db.adapter.AssetRepositoryAdapter;
import fr.globalmarket.adapter.out.db.adapter.PriceHistoryRepositoryAdapter;
import fr.globalmarket.adapter.out.db.adapter.TransactionRepositoryAdapter;
import fr.globalmarket.adapter.out.db.adapter.UserRepositoryAdapter;
import fr.globalmarket.adapter.out.db.mapper.AssetMapper;
import fr.globalmarket.adapter.out.db.mapper.PriceHistoryMapper;
import fr.globalmarket.adapter.out.db.mapper.TransactionMapper;
import fr.globalmarket.adapter.out.db.mapper.UserMapper;
import fr.globalmarket.adapter.out.db.repository.AssetRepository;
import fr.globalmarket.adapter.out.db.repository.PriceHistoryRepository;
import fr.globalmarket.adapter.out.db.repository.TransactionRepository;
import fr.globalmarket.adapter.out.db.repository.UserRepository;
import fr.globalmarket.application.port.out.AssetRepositoryPort;
import fr.globalmarket.application.port.out.PriceHistoryPort;
import fr.globalmarket.application.port.out.TransactionRepositoryPort;
import fr.globalmarket.application.port.out.UserRepositoryPort;
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
