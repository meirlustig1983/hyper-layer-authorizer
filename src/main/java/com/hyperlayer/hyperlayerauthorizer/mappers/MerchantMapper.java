package com.hyperlayer.hyperlayerauthorizer.mappers;

import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dto.Merchant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper {

    public Merchant mapToMerchant(DbMerchant dbMerchant) {
        if (dbMerchant == null) {
            return null;
        }
        return new Merchant(dbMerchant.getMerchantId().toString(),
                dbMerchant.getMerchantName());
    }

    public DbMerchant mapToDbMerchant(Merchant merchant) {
        if (merchant == null) {
            return null;
        }
        return new DbMerchant()
                .setMerchantId(merchant.merchantId() != null ? new ObjectId(merchant.merchantId()) : null)
                .setMerchantName(merchant.merchantName());
    }
}
