package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import com.hyperlayer.hyperlayerauthorizer.evaluators.Evaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AuthorizationService {

    private final Map<String, Evaluator> evaluatorsMap;

    public AuthorizationService(List<Evaluator> evaluators) {
        this.evaluatorsMap = new HashMap<>();
        evaluators.forEach(evaluator -> {
            String className = evaluator.getClass().getSimpleName();
            log.info("loading new evaluator class. name: {}", className);
            evaluatorsMap.put(className, evaluator);
        });
    }

    public boolean authorize(DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        boolean isAuthorized = true;
        for (RewardRuleData rewardRuleData : dbReward.getRules()) {
            String className = rewardRuleData.getClassEvaluator();
            Evaluator evaluator = evaluatorsMap.get(className);
            isAuthorized &= evaluator.evaluate(rewardRuleData, dbCustomer, dbReward, dbMerchant, request);
        }
        log.info("execute authorize service. " +
                "customerId: {}, rewardId: {}, merchantId: {}, date: {}, amount: {}, result: {}",
                request.customerId(), request.rewardId(), request.merchantId(),
                request.date(), request.amount(), isAuthorized);
        return isAuthorized;
    }
}
