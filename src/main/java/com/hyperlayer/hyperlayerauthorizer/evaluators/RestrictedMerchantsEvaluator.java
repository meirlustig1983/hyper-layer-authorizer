package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.ListRuleData;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestrictedMerchantsEvaluator implements Evaluator {
    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        ListRuleData ruleData = (ListRuleData) rewardRuleData;
        List<String> values = ruleData.getValues();
        boolean result = !values.contains(dbMerchant.getMerchantId().toString());
        log.info("restricted merchants evaluate. merchantId: {}, restricted-merchants: {}, result: {}", request.merchantId(), values, result);
        return result;
    }
}