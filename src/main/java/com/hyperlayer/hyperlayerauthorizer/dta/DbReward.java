package com.hyperlayer.hyperlayerauthorizer.dta;

import com.hyperlayer.hyperlayerauthorizer.dto.RewardRuleData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "rewards")
public class DbReward {

    @Id
    private ObjectId rewardId;

    @NonNull
    private String name;

    @NonNull
    private Double balance;

    private List<RewardRuleData> rules;

    @NonNull
    private LocalDateTime createdDate;

    @NonNull
    private LocalDateTime updateDate;
}
