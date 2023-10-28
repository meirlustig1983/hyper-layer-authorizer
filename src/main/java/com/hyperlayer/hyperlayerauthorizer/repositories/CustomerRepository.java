package com.hyperlayer.hyperlayerauthorizer.repositories;

import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<DbCustomer, ObjectId> {
}
