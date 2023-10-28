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
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "customers")
public class DbCustomer implements Serializable {

    @Id
    private ObjectId customerId;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private LocalDateTime birthDate;
    private Set<ObjectId> rewards;
    @NonNull
    private LocalDateTime createdDate;
}