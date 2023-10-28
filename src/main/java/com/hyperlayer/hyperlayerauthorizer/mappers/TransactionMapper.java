package com.hyperlayer.hyperlayerauthorizer.mappers;

import com.hyperlayer.hyperlayerauthorizer.dta.DbTransaction;
import com.hyperlayer.hyperlayerauthorizer.dto.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction mapToTransaction(DbTransaction dbTransaction) {
        if (dbTransaction == null) {
            return null;
        }

        return new Transaction(dbTransaction.getCustomerId().toString(),
                dbTransaction.getMerchantId().toString(),
                dbTransaction.getAmount(),
                dbTransaction.getCreatedDate());
    }
}