package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

public record Customer(String customerId,
                       @NonNull String firstName,
                       @NonNull String lastName,
                       @NonNull LocalDateTime birthDate,
                       Set<String> rewards) {
}