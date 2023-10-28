package com.hyperlayer.hyperlayerauthorizer.dto;

import jakarta.validation.constraints.Min;
import lombok.NonNull;

import java.util.List;

public record Reward(String rewardId,
                     @NonNull String name,
                     @NonNull @Min(1) Double balance,
                     List<RewardRuleData> rules) {
}
