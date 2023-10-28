package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.RangeValuesRuleData;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class AllowedHoursEvaluator implements Evaluator {
    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        try {
            RangeValuesRuleData ruleData = (RangeValuesRuleData) rewardRuleData;
            String start = ruleData.getStart();
            String end = ruleData.getEnd();

            // Define a DateTimeFormatter for parsing the input string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h a");
            // Parse the string to LocalTime
            LocalTime startLocalTime = LocalTime.parse(start, formatter);
            LocalTime endLocalTime = LocalTime.parse(end, formatter);
            boolean result = isBetweenTimes(request.date().toLocalTime(), startLocalTime, endLocalTime);
            log.info("allowed hours evaluate. request-time: {}, start-time: {}, end-time:{}, result: {}", request.date().toLocalTime(), start, end, result);
            return result;
        } catch (Exception e) {
            log.info("allowed hours evaluate. request-time: {}, result: true", request.date().toLocalTime());
            return true;
        }
    }

    public boolean isBetweenTimes(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }
}