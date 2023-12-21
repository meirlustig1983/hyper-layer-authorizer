package com.hyperlayer.hyperlayerauthorizer.facade;

import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dta.DbMerchant;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dta.DbTransaction;
import com.hyperlayer.hyperlayerauthorizer.repositories.CustomerRepository;
import com.hyperlayer.hyperlayerauthorizer.repositories.MerchantRepository;
import com.hyperlayer.hyperlayerauthorizer.repositories.RewardRepository;
import com.hyperlayer.hyperlayerauthorizer.repositories.TransactionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DataFacade {

    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final RewardRepository rewardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DataFacade(CustomerRepository customerRepository, MerchantRepository merchantRepository,
                      RewardRepository rewardRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.rewardRepository = rewardRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<DbCustomer> getCustomers() {
        return customerRepository.findAll();
    }

    @Cacheable(value = "customers", key = "#customerId")
    public Optional<DbCustomer> getCustomerById(ObjectId customerId) {
        return customerRepository.findById(customerId);
    }

    @CachePut(value = "customers", key = "#result.get().customerId")
    public Optional<DbCustomer> saveCustomer(DbCustomer dbCustomer) {
        DbCustomer newCustomer = customerRepository.save(dbCustomer);
        return customerRepository.findById(newCustomer.getCustomerId());
    }

    public void deleteCustomerById(ObjectId customerId) {
        customerRepository.deleteById(customerId);
    }

    public List<DbMerchant> getMerchants() {
        return merchantRepository.findAll();
    }

    @Cacheable(value = "merchants", key = "#merchantId")
    public Optional<DbMerchant> getMerchantById(ObjectId merchantId) {
        return merchantRepository.findById(merchantId);
    }

    @CachePut(value = "merchants", key = "#result.get().merchantId")
    public Optional<DbMerchant> saveMerchant(DbMerchant dbMerchant) {
        DbMerchant newMerchant = merchantRepository.save(dbMerchant);
        return merchantRepository.findById(newMerchant.getMerchantId());
    }

    public void deleteMerchantById(ObjectId merchantId) {
        merchantRepository.deleteById(merchantId);
    }

    public List<DbReward> getRewards() {
        return rewardRepository.findAll();
    }

    @Cacheable(value = "rewards", key = "#rewardId")
    public Optional<DbReward> getRewardById(ObjectId rewardId) {
        return rewardRepository.findById(rewardId);
    }

    @CachePut(value = "rewards", key = "#result.get().rewardId")
    public Optional<DbReward> saveReward(DbReward dbReward) {
        DbReward newReward = rewardRepository.save(dbReward);
        return rewardRepository.findById(newReward.getRewardId());
    }

    public void deleteRewardById(ObjectId rewardId) {
        rewardRepository.deleteById(rewardId);
    }

    public List<DbTransaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public List<DbTransaction> getTransactions(ObjectId customerId) {
        return transactionRepository.findAllByCustomerId(customerId);
    }

    public List<DbTransaction> getTransactions(ObjectId merchantId, ObjectId customerId) {
        return transactionRepository.findAllByCustomerIdAndMerchantId(customerId, merchantId);
    }

    public List<DbTransaction> getTransactions(ObjectId customerId, ObjectId merchantId,
                                               LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findAllByCustomerIdAndMerchantIdAndCreatedDateBetween(customerId, merchantId, startDate, endDate);
    }

    public Optional<DbTransaction> saveTransaction(DbTransaction dbTransaction) {
        DbTransaction newTransaction = transactionRepository.save(dbTransaction);
        return transactionRepository.findById(newTransaction.getTransactionId());
    }

    public void deleteTransactionsByCustomerId(ObjectId customerId) {
        transactionRepository.deleteAllByCustomerId(customerId);
    }
}
