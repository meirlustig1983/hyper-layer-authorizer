package com.hyperlayer.hyperlayerauthorizer.controllers.requests;

import lombok.NonNull;

import java.util.List;

public record ListRuleRequest(@NonNull String classEvaluator,
                              @NonNull List<String> list) {
}