package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RewardSharePermissionEvaluator implements Evaluator {
    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward,
                            DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        boolean result = dbCustomer.getRewards().contains(dbReward.getRewardId());
        log.info("reward share permission evaluate. rewardId: {}, customer-rewards: {}, result: {}", request.rewardId(), dbCustomer.getRewards(), result);
        return result;
    }
}