package com.hyperlayer.hyperlayerauthorizer.controllers.requests;

import lombok.NonNull;

public record RangeValuesRuleRequest(@NonNull String classEvaluator,
                                     @NonNull String start,
                                     @NonNull String end) {
}