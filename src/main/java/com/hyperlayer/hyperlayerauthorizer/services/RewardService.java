package com.hyperlayer.hyperlayerauthorizer.services;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ListRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.RangeValuesRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TwoValuesRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.dta.DbReward;
import com.hyperlayer.hyperlayerauthorizer.dto.*;
import com.hyperlayer.hyperlayerauthorizer.facade.DataFacade;
import com.hyperlayer.hyperlayerauthorizer.mappers.RewardMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RewardService {
    private final DataFacade dataFacade;
    private final RewardMapper rewardMapper;

    public RewardService(DataFacade dataFacade, RewardMapper rewardMapper) {
        this.dataFacade = dataFacade;
        this.rewardMapper = rewardMapper;
    }

    public List<Reward> findAll() {
        List<Reward> data = dataFacade.getRewards().stream().map(rewardMapper::mapToReward).toList();
        log.info("get all rewards data. size: {}", data.size());
        return data;
    }

    public Reward findById(String rewardId) {
        log.info("get reward data. rewardId: {}", rewardId);
        Optional<DbReward> result = dataFacade.getRewardById(new ObjectId(rewardId));
        return result.map(rewardMapper::mapToReward).orElse(null);
    }

    public Reward save(Reward reward) {
        DbReward dbReward = rewardMapper.mapToDbReward(reward);
        List<RewardRuleData> rules = List.of(new RewardRuleData(ObjectId.get().toString(), "InsufficientFundsEvaluator"), new RewardRuleData(ObjectId.get().toString(), "RewardSharePermissionEvaluator"));

        dbReward.setCreatedDate(LocalDateTime.now());
        dbReward.setUpdateDate(LocalDateTime.now());
        dbReward.setRules(rules);

        Optional<DbReward> result = dataFacade.saveReward(dbReward);
        log.info("save new reward data. rewardId: {}", result.<Object>map(DbReward::getRewardId).orElse(null));
        return result.map(rewardMapper::mapToReward).orElse(null);
    }

    public Reward addTwoValuesRule(String rewardId, TwoValuesRuleRequest request) {
        Optional<DbReward> result = dataFacade.getRewardById(new ObjectId(rewardId));
        if (result.isPresent()) {
            DbReward dbReward = result.get();
            List<RewardRuleData> rules = dbReward.getRules();
            if (rules == null) {
                rules = new ArrayList<>();
            }
            rules.add(new TwoValuesRuleData(ObjectId.get().toString(), request.classEvaluator(), request.valueA(), request.valueB()));
            dbReward.setRules(rules);
            log.info("update reward with new rule. classEvaluator: {}, valueA: {}, valueB: {}", request.classEvaluator(), request.valueA(), request.valueB());
            return dataFacade.saveReward(dbReward).map(rewardMapper::mapToReward).orElse(null);
        } else {
            return null;
        }
    }

    public Reward addListRule(String rewardId, ListRuleRequest request) {
        Optional<DbReward> result = dataFacade.getRewardById(new ObjectId(rewardId));
        if (result.isPresent()) {
            DbReward dbReward = result.get();
            List<RewardRuleData> rules = dbReward.getRules();
            if (rules == null) {
                rules = new ArrayList<>();
            }
            rules.add(new ListRuleData(ObjectId.get().toString(), request.classEvaluator(), request.list()));
            dbReward.setRules(rules);
            log.info("update reward with new rule. classEvaluator: {}, list: {}", request.classEvaluator(), request.list());
            return dataFacade.saveReward(dbReward).map(rewardMapper::mapToReward).orElse(null);
        } else {
            return null;
        }
    }

    public Reward addRangeValuesRule(String rewardId, RangeValuesRuleRequest request) {
        Optional<DbReward> result = dataFacade.getRewardById(new ObjectId(rewardId));
        if (result.isPresent()) {
            DbReward dbReward = result.get();
            List<RewardRuleData> rules = dbReward.getRules();
            if (rules == null) {
                rules = new ArrayList<>();
            }
            rules.add(new RangeValuesRuleData(ObjectId.get().toString(), request.classEvaluator(), request.start(), request.end()));
            dbReward.setRules(rules);
            log.info("update reward with new rule. classEvaluator: {}, start: {}, end: {}", request.classEvaluator(), request.start(), request.end());
            return dataFacade.saveReward(dbReward).map(rewardMapper::mapToReward).orElse(null);
        } else {
            return null;
        }
    }

    public Reward deleteRule(String rewardId, String ruleId) {
        Optional<DbReward> result = dataFacade.getRewardById(new ObjectId(rewardId));
        if (result.isPresent()) {
            DbReward dbReward = result.get();
            List<RewardRuleData> rules = dbReward.getRules();
            if (!CollectionUtils.isEmpty(rules)) {
                rules = rules.stream().filter(rule -> rule.getRuleId().equals(ruleId)).toList();
                dbReward.setRules(rules);
                log.info("delete rule from reward. rewardId: {}, ruleId: {}", rewardId, ruleId);
            }
            return dataFacade.saveReward(dbReward).map(rewardMapper::mapToReward).orElse(null);
        } else {
            return null;
        }
    }

    public void deleteById(String rewardId) {
        log.info("delete reward data. rewardId: {}", rewardId);
        dataFacade.deleteRewardById(new ObjectId(rewardId));
    }
}