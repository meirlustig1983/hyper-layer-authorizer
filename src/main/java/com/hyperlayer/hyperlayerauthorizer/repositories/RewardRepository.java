package com.hyperlayer.hyperlayerauthorizer.repositories;

import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<DbReward, ObjectId> {
}