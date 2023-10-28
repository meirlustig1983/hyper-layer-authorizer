package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.RangeValuesRuleData;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Slf4j
@Component
public class CustomerAgeEvaluator implements Evaluator {
    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        RangeValuesRuleData ruleData = (RangeValuesRuleData) rewardRuleData;

        String start = ruleData.getStart();
        String end = ruleData.getEnd();

        LocalDateTime birthDate = dbCustomer.getBirthDate();
        // Get the current date as a LocalDateTime
        LocalDateTime currentDate = LocalDateTime.now();
        // Calculate the age
        int age = calculateAge(birthDate, currentDate);

        boolean result = false;
        if (StringUtils.hasLength(start) && StringUtils.hasLength(end)) {
            int startInt = Integer.parseInt(start);
            int endInt = Integer.parseInt(end);
            if (startInt > 0 && endInt > 0) {
                result = startInt >= age && age <= endInt;
            } else if (startInt < 0) {
                result = age <= endInt;
            } else {
                result = startInt >= age;
            }
        }
        log.info("customer age evaluate. customer-age: {}, from: {}, to: {}, result: {}", age, start, end, result);
        return result;
    }

    public int calculateAge(LocalDateTime birthDate, LocalDateTime currentDate) {
        LocalDate birthDateAsLocalDate = birthDate.toLocalDate();
        LocalDate currentDateAsLocalDate = currentDate.toLocalDate();
        return Period.between(birthDateAsLocalDate, currentDateAsLocalDate).getYears();
    }
}