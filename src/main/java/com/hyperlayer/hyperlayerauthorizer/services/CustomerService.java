package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import com.hyperlayer.hyperlayerauthorizer.mappers.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService {

    private final DataFacade dataFacade;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(DataFacade dataFacade, CustomerMapper customerMapper) {
        this.dataFacade = dataFacade;
        this.customerMapper = customerMapper;
    }

    public List<Customer> findAll() {
        List<Customer> data = dataFacade.getCustomers().stream().map(customerMapper::mapToCustomer).toList();
        log.info("get all customers data. size: {}", data.size());
        return data;
    }

    public Customer findById(String customerId) {
        log.info("get customer data. customerId: {}", customerId);
        Optional<DbCustomer> result = dataFacade.getCustomerById(new ObjectId(customerId));
        return result.map(customerMapper::mapToCustomer).orElse(null);
    }

    public Customer save(Customer customer) {
        DbCustomer dbCustomer = customerMapper.mapToDbCustomer(customer);
        dbCustomer.setCreatedDate(LocalDateTime.now());
        Optional<DbCustomer> result = dataFacade.saveCustomer(dbCustomer);
        log.info("save new customer data. customerId: {}",
                result.<Object>map(DbCustomer::getCustomerId).orElse(null));
        return result.map(customerMapper::mapToCustomer).orElse(null);
    }
}
