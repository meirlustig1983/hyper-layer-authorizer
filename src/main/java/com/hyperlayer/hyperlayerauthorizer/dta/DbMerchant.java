package com.hyperlayer.hyperlayerauthorizer.dta;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "merchants")
public class DbMerchant {

    @Id
    private ObjectId merchantId;
    @NonNull
    private String merchantName;
    @NonNull
    private LocalDateTime createdDate;
}