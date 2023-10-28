package com.hyperlayer.hyperlayerauthorizer.repositories;

import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MerchantRepository  extends MongoRepository<DbMerchant, ObjectId> {
}
