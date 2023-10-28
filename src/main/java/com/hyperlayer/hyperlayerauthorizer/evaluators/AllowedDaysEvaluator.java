package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.ListRuleData;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;

@Slf4j
@Component
public class AllowedDaysEvaluator implements Evaluator {
    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        ListRuleData ruleData = (ListRuleData) rewardRuleData;
        List<String> values = ruleData.getValues();

        // Get the DayOfWeek enum
        DayOfWeek dayOfWeek = request.date().getDayOfWeek();
        String dayOfWeekString = dayOfWeek.toString();
        String dayOfWeekLocalized = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
        boolean result = values.contains(dayOfWeekString) || values.contains(dayOfWeekString.toLowerCase()) || values.contains(dayOfWeekLocalized);
        log.info("allowed days evaluate. day: {}, allowed-days: {}, result: {}", dayOfWeekString, values, result);
        return result;
    }
}