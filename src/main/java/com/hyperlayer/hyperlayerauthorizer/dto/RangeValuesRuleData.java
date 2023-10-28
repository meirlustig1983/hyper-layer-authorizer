package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.Getter;

@Getter
public class RangeValuesRuleData extends RewardRuleData {

    private final String start;
    private final String end;

    public RangeValuesRuleData(String ruleId, String classEvaluator, String start, String end) {
        super(ruleId, classEvaluator);
        this.start = start;
        this.end = end;
    }
}
