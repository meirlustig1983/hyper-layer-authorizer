package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;

public interface Evaluator {
    boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward,
                     DbMerchant dbMerchant, TransactionAuthorizationRequest request);
}
