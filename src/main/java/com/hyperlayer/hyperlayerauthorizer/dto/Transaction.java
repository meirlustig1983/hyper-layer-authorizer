package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record Transaction(@NonNull String customerId, @NonNull String merchantId,
                          @NonNull Double amount, @NonNull LocalDateTime createdDate) {
}
