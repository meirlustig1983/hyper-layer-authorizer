package com.hyperlayer.hyperlayerauthorizer.controllers.requests;

import lombok.NonNull;

public record ShareRewardRequest(@NonNull String customerId,
                                 @NonNull String rewardId) {
}