package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class ListRuleData extends RewardRuleData implements Serializable {

    private final List<String> values;

    public ListRuleData(String ruleId, String classEvaluator, List<String> values) {
        super(ruleId, classEvaluator);
        this.values = values;
    }
}