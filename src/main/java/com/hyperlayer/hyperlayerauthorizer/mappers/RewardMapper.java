package com.hyperlayer.hyperlayerauthorizer.mappers;

import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.Reward;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RewardMapper {

    public Reward mapToReward(DbReward dbReward) {
        if (dbReward == null) {
            return null;
        }

        List<RewardRuleData> rules = dbReward.getRules() != null ? dbReward.getRules() : Collections.emptyList();

        return new Reward(dbReward.getRewardId().toString(),
                dbReward.getName(),
                dbReward.getBalance(),
                rules);
    }

    public DbReward mapToDbReward(Reward reward) {
        if (reward == null) {
            return null;
        }

        List<RewardRuleData> rules = reward.rules() != null ? reward.rules() : Collections.emptyList();

        return new DbReward()
                .setRewardId(reward.rewardId() != null ? new ObjectId(reward.rewardId()) : null)
                .setName(reward.name())
                .setBalance(reward.balance())
                .setRules(rules);
    }
}