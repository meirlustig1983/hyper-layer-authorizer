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
public class TransactionsLimitationForPeriodTimeEvaluator implements Evaluator {

    private final DataFacade dataFacade;

    public TransactionsLimitationForPeriodTimeEvaluator(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    @Override
    public boolean evaluate(RewardRuleData rewardRuleData, DbCustomer dbCustomer, DbReward dbReward, DbMerchant dbMerchant, TransactionAuthorizationRequest request) {

        TwoValuesRuleData ruleData = (TwoValuesRuleData) rewardRuleData;
        double transactionsLimitation = Double.parseDouble(ruleData.getValueA());
        int periodDays = Integer.parseInt(ruleData.getValueB());

        LocalDateTime currentDateTime = request.date();
        LocalDateTime startDateTime = currentDateTime.minusDays(periodDays);

        List<DbTransaction> transactions = dataFacade.getTransactions(dbCustomer.getCustomerId(), dbMerchant.getMerchantId(), startDateTime, currentDateTime);

        boolean result = !(transactions.size() > transactionsLimitation);
        log.info("transactions limitation for period time evaluate. transactions-limitation: {}, transactions-count: {}, result: {}", transactionsLimitation, transactions.size(), result);
        return result;
    }
}