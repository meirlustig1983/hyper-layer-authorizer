package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardRuleData {

    private final String ruleId;

    private final String classEvaluator;

    public RewardRuleData(String ruleId, String classEvaluator) {
        this.ruleId = ruleId;
        this.classEvaluator = classEvaluator;
    }
}
