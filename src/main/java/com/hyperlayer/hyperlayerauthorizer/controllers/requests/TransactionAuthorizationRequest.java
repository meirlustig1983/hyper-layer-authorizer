package com.hyperlayer.hyperlayerauthorizer.controllers.requests;

import jakarta.validation.constraints.Min;
import lombok.NonNull;

import java.time.LocalDateTime;

public record TransactionAuthorizationRequest(@NonNull String customerId,
                                              @NonNull String rewardId,
                                              @NonNull String merchantId,
                                              @NonNull @Min(1) Double amount,
                                              @NonNull LocalDateTime date) {
}