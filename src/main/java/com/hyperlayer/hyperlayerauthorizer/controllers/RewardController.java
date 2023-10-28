package com.hyperlayer.hyperlayerauthorizer.controllers;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ListRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.RangeValuesRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TwoValuesRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.dto.Reward;
import com.hyperlayer.hyperlayerauthorizer.services.RewardService;
import com.hyperlayer.hyperlayerauthorizer.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService service;

    @Autowired
    public RewardController(RewardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Reward>> getAllRewards() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<Reward> getRewardById(@PathVariable String rewardId) {
        Reward reward = service.findById(rewardId);
        if (reward != null) {
            return ResponseEntity.ok(reward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Reward> createReward(@RequestBody Reward reward) {
        Reward newReward = service.save(reward);
        if (newReward != null) {
            return ResponseEntity.created(ControllerHelper.getLocation("/api/rewards")).body(newReward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{rewardId}/rules/two-values")
    public ResponseEntity<Reward> addTwoValuesRule(@PathVariable String rewardId, @RequestBody TwoValuesRuleRequest request) {
        Reward reward = service.addTwoValuesRule(rewardId, request);
        if (reward != null) {
            return ResponseEntity.ok(reward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{rewardId}/rules/list")
    public ResponseEntity<Reward> addListRule(@PathVariable String rewardId, @RequestBody ListRuleRequest request) {
        Reward reward = service.addListRule(rewardId, request);
        if (reward != null) {
            return ResponseEntity.ok(reward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{rewardId}/rules/range")
    public ResponseEntity<Reward> addRangeValuesRule(@PathVariable String rewardId, @RequestBody RangeValuesRuleRequest request) {
        Reward reward = service.addRangeValuesRule(rewardId, request);
        if (reward != null) {
            return ResponseEntity.ok(reward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{rewardId}/rules/{ruleId}")
    public ResponseEntity<Reward> deleteRule(@PathVariable String rewardId, @PathVariable String ruleId) {
        Reward reward = service.deleteRule(rewardId, ruleId);
        if (reward != null) {
            return ResponseEntity.ok(reward);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}