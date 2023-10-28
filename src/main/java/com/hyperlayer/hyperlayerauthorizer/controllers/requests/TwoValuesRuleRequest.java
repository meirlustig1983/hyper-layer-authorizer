package com.hyperlayer.hyperlayerauthorizer.controllers.requests;

import lombok.NonNull;

public record TwoValuesRuleRequest(@NonNull String classEvaluator,
                                   @NonNull String valueA,
                                   @NonNull String valueB) {
}