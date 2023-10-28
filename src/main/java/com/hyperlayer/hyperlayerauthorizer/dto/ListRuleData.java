package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ListRuleData extends RewardRuleData {

    private final List<String> values;

    public ListRuleData(String ruleId, String classEvaluator, List<String> values) {
        super(ruleId, classEvaluator);
        this.values = values;
    }
}