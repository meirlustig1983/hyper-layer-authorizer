package com.hyperlayer.hyperlayerauthorizer.repositories;

import com.hyperlayer.hyperlayerauthorizer.dta.DbTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<DbTransaction, ObjectId> {
    List<DbTransaction> findAllByCustomerId(ObjectId customerId);
    List<DbTransaction> findAllByCustomerIdAndMerchantId(ObjectId merchantId, ObjectId customerId);
    List<DbTransaction> findAllByCustomerIdAndMerchantIdAndCreatedDateBetween(ObjectId merchantId, ObjectId customerId, LocalDateTime startDate, LocalDateTime endDate);
    void deleteAllByCustomerId(ObjectId customerId);
}