package com.hyperlayer.hyperlayerauthorizer.evaluators;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dta.DbTransaction;
import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import com.hyperlayer.hyperlayerauthorizer.dto.TwoValuesRuleData;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class AmountLimitationForPeriodTimeEvaluator implements Evaluator {

    private final DataFacade dataFacade;

    public AmountLimitationForPeriodTimeEvaluator(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {
        TwoValuesRuleData ruleData = (TwoValuesRuleData) rewardRuleData;
        double amountLimitation = Double.parseDouble(ruleData.getValueA());
        int periodDays = Integer.parseInt(ruleData.getValueB());

        LocalDateTime currentDateTime = request.date();
        LocalDateTime startDateTime = currentDateTime.minusDays(periodDays);

        List<DbTransaction> transactions = dataFacade.getTransactions(dbCustomer.getCustomerId(), dbMerchant.getMerchantId(), startDateTime, currentDateTime);
        double sum = transactions.stream().mapToDouble(DbTransaction::getAmount).sum();

        boolean result = !(sum > amountLimitation);
        log.info("amount limitation for period time evaluate. amount-limitation: {}, period-days: {}, sum: {}, result: {}", amountLimitation, periodDays, sum, result);
        return result;
    }
}

