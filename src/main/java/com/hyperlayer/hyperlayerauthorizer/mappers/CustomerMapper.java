package com.hyperlayer.hyperlayerauthorizer.mappers;

import com.hyperlayer.hyperlayerauthorizer.dta.DbCustomer;
import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {
    public Customer mapToCustomer(DbCustomer dbCustomer) {
        if (dbCustomer == null) {
            return null;
        }

        Set<String> rewards = dbCustomer.getRewards() != null ?
                dbCustomer.getRewards().stream().map(ObjectId::toString).collect(Collectors.toSet()) : Collections.emptySet();

        return new Customer(
                dbCustomer.getCustomerId().toString(),
                dbCustomer.getFirstName(),
                dbCustomer.getLastName(),
                dbCustomer.getBirthDate(),
                rewards
        );
    }

    public DbCustomer mapToDbCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }

        Set<ObjectId> rewards = customer.rewards() != null ?
                customer.rewards().stream().map(ObjectId::new).collect(Collectors.toSet()) : Collections.emptySet();

        return new DbCustomer()
                .setCustomerId(customer.customerId() != null ? new ObjectId(customer.customerId()) : null)
                .setFirstName(customer.firstName())
                .setLastName(customer.lastName())
                .setBirthDate(customer.birthDate())
                .setRewards(rewards);
    }
}