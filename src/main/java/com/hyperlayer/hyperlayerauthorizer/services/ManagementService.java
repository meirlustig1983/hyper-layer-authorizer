package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ShareRewardRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dta.DbTransaction;
import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import com.hyperlayer.hyperlayerauthorizer.mappers.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ManagementService {

    private final DataFacade dataFacade;
    private final CustomerMapper customerMapper;
    private final AuthorizationService service;

    @Autowired
    public ManagementService(DataFacade dataFacade, CustomerMapper customerMapper, AuthorizationService service) {
        this.dataFacade = dataFacade;
        this.customerMapper = customerMapper;
        this.service = service;
    }

    public Customer shareReward(ShareRewardRequest request) {
        log.info("share reward request. customerId: {}, rewardId: {}", request.customerId(), request.rewardId());
        ObjectId customerId = new ObjectId(request.customerId());
        ObjectId rewardId = new ObjectId(request.rewardId());

        Optional<DbCustomer> customerResult = dataFacade.getCustomerById(customerId);
        Optional<DbReward> rewardResult = dataFacade.getRewardById(rewardId);

        if (customerResult.isPresent() && rewardResult.isPresent()) {
            DbCustomer dbCustomer = customerResult.get();
            Set<ObjectId> rewards = dbCustomer.getRewards();
            if (rewards == null) {
                rewards = new HashSet<>();
            }
            rewards.add(rewardId);
            dbCustomer.setRewards(rewards);
            customerResult = dataFacade.saveCustomer(dbCustomer);
            return customerResult.map(customerMapper::mapToCustomer).orElse(null);
        } else {
            throw new IllegalArgumentException("failed to achieve data with input arguments");
        }
    }

    public Boolean authorize(TransactionAuthorizationRequest request) {

        ObjectId customerId = new ObjectId(request.customerId());
        ObjectId rewardId = new ObjectId(request.rewardId());
        ObjectId merchantId = new ObjectId(request.merchantId());
        Double amount = request.amount();

        Optional<DbCustomer> customerResult = dataFacade.getCustomerById(customerId);
        Optional<DbReward> rewardResult = dataFacade.getRewardById(rewardId);
        Optional<DbMerchant> merchantResult = dataFacade.getMerchantById(merchantId);

        if (customerResult.isPresent() && rewardResult.isPresent() && merchantResult.isPresent()) {
            if (service.authorize(customerResult.get(), rewardResult.get(), merchantResult.get(), request)) {
                DbReward dbReward = rewardResult.get();
                dbReward.setBalance(dbReward.getBalance() - amount);
                dataFacade.saveReward(dbReward);
                dataFacade.saveTransaction(new DbTransaction().setCustomerId(customerId).setMerchantId(merchantId).setAmount(amount).setCreatedDate(LocalDateTime.now()));
                log.info("authorize request. customerId: {}, rewardId: {}, merchantId: {}, " + "amount: {}, date: {}, result: true", request.customerId(), request.rewardId(), request.merchantId(), request.amount(), request.date());
                return true;
            }
        }
        log.info("authorize request. customerId: {}, rewardId: {}, merchantId: {}, " + "amount: {}, date: {}, result: false", request.customerId(), request.rewardId(), request.merchantId(), request.amount(), request.date());
        return false;
    }
}