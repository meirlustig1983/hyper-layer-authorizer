package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.dto.Transaction;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import com.hyperlayer.hyperlayerauthorizer.mappers.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransactionService {

    private final DataFacade dataFacade;
    private final TransactionMapper transactionMapper;

    public TransactionService(DataFacade dataFacade, TransactionMapper transactionMapper) {
        this.dataFacade = dataFacade;
        this.transactionMapper = transactionMapper;
    }

    public List<Transaction> findAll() {
        List<Transaction> data = dataFacade.getTransactions().stream().map(transactionMapper::mapToTransaction).toList();
        log.info("get all transaction data. size: {}", data.size());
        return data;
    }

    public List<Transaction> findAll(String customerId) {
        List<Transaction> data = dataFacade.getTransactions(new ObjectId(customerId)).stream().map(transactionMapper::mapToTransaction).toList();
        log.info("get all transaction data. customerId: {}, size: {}", customerId, data.size());
        return data;
    }

    public List<Transaction> findAll(String customerId, String merchantId) {
        List<Transaction> data = dataFacade.getTransactions(new ObjectId(customerId), new ObjectId(merchantId)).stream().map(transactionMapper::mapToTransaction).toList();
        log.info("get all transaction data. customerId: {}, merchantId: {}, size: {}", customerId, merchantId, data.size());
        return data;
    }

    public void deleteAllByCustomerId(String customerId) {
        log.info("delete transactions data. customerId: {}", customerId);
        dataFacade.deleteTransactionsByCustomerId(new ObjectId(customerId));
    }
}