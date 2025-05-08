package fr.globalmarket.util;

import fr.globalmarket.domain.core.model.AssetType;
import fr.globalmarket.domain.core.model.OrderType;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class DataConstants {

    public final String EMAIL_USER_1 = "dbcooper50@gmail.com";
    public final String EMAIL_USER_2 = "skywanderer@gmail.com";
    public final String PASSWORD_USER_1 = "Atr?Ht5L3&mxFRzzrMMm";
    public final BigDecimal INITIAL_CAPITAL = new BigDecimal("10000.00");

    public final AssetType STOCK_ASSET_TYPE = AssetType.STOCK;
    public final AssetType CRYPTO_ASSET_TYPE = AssetType.CRYPTO;

    public final OrderType BUY_ORDER = OrderType.BUY;
    public final OrderType SELL_ORDER = OrderType.SELL;

    public final String BITCOIN_ASSET_NAME = "BITCOIN";
    public final BigDecimal BITCOIN_PRICE_1 = new BigDecimal("91372.90");
    public final BigDecimal BITCOIN_PRICE_2 = new BigDecimal("94732.56");

    public final String ELI_LILLY_ASSET_NAME = "ELI LILLY";
    public final BigDecimal ELI_LILLY_ASSET_PRICE_1 = new BigDecimal("776.62");
    public final BigDecimal ELI_LILLY_ASSET_PRICE_2 = new BigDecimal("891.27");

    public final String MICROSOFT_ASSET_NAME = "MICROSOFT";
    public final BigDecimal MICROSOFT_ASSET_PRICE_1 = new BigDecimal("435.00");
    public final BigDecimal MICROSOFT_ASSET_PRICE_2 = new BigDecimal("447.31");

}
