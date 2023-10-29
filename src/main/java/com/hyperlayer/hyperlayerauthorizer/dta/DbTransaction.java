package com.hyperlayer.hyperlayerauthorizer.dta;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "transactions")
public class DbTransaction implements Serializable {

    @Id
    private ObjectId transactionId;
    @NonNull
    private ObjectId customerId;
    @NonNull
    private ObjectId merchantId;
    @NonNull
    private Double amount;
    @NonNull
    private LocalDateTime createdDate;
}