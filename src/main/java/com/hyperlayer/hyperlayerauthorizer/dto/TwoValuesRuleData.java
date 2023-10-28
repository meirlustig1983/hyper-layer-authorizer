package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class TwoValuesRuleData extends RewardRuleData implements Serializable {

    private final String valueA;
    private final String valueB;

    public TwoValuesRuleData(String ruleId, String classEvaluator, String valueA, String valueB) {
        super(ruleId, classEvaluator);
        this.valueA = valueA;
        this.valueB = valueB;
    }
}