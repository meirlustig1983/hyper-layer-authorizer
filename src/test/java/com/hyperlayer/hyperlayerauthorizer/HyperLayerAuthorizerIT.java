package com.hyperlayer.hyperlayerauthorizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ListRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.RangeValuesRuleRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ShareRewardRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import com.hyperlayer.hyperlayerauthorizer.dto.Merchant;
import com.hyperlayer.hyperlayerauthorizer.dto.Reward;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HyperLayerAuthorizerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test() throws Exception {

        // CREATE CUSTOMER

        LocalDate date = LocalDate.of(1983, 11, 15);
        LocalTime time = LocalTime.of(12, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        Customer customer = new Customer(null,
                "Meir",
                "Lustig",
                localDateTime,
                null);

        ResultActions customersResult = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.birthDate").value("1983-11-15T12:00:00"))
                .andExpect(jsonPath("$.rewards").isEmpty());

        String customersJson = customersResult.andReturn().getResponse().getContentAsString();
        String customerId = JsonPath.read(customersJson, "$.customerId").toString();

        // CREATE FIRST MERCHANT

        Merchant sony = new Merchant(null, "Sony");
        ResultActions sonyResult = mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sony)))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.merchantId").isNotEmpty())
                .andExpect(jsonPath("$.merchantName").value("Sony"));

        String sonyJson = sonyResult.andReturn().getResponse().getContentAsString();
        String sonyMerchantId = JsonPath.read(sonyJson, "$.merchantId").toString();

        // CREATE SECOND MERCHANT

        Merchant walmart = new Merchant(null, "Walmart");
        ResultActions walmartResult = mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walmart)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.merchantId").isNotEmpty())
                .andExpect(jsonPath("$.merchantName").value("Walmart"));

        String walmartJson = walmartResult.andReturn().getResponse().getContentAsString();
        String walmartMerchantId = JsonPath.read(walmartJson, "$.merchantId").toString();


        // CREATE FIRST REWARD

        Reward sonyReward = new Reward(null, "SONY-001", 1000.0, null);
        ResultActions sonyRewardResult = mockMvc.perform(post("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sonyReward)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());

        String sonyRewardJson = sonyRewardResult.andReturn().getResponse().getContentAsString();
        String sonyRewardId = JsonPath.read(sonyRewardJson, "$.rewardId").toString();

        // ADD SOME RULES

        ListRuleRequest allowedDaysList = new ListRuleRequest("AllowedDaysEvaluator",
                List.of("MONDAY", "FRIDAY"));

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/list", sonyRewardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allowedDaysList)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());

        RangeValuesRuleRequest rangeValuesRuleRequest =
                new RangeValuesRuleRequest("AllowedHoursEvaluator",
                        "12 PM", "6 PM");

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/range", sonyRewardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rangeValuesRuleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());

        ListRuleRequest allowedMerchantList =
                new ListRuleRequest("AllowedMerchantsEvaluator", List.of(sonyMerchantId));

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/list", sonyRewardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allowedMerchantList)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());


        // CREATE SECOND REWARD

        Reward walmartReward = new Reward(null, "WALMART-001", 1000.0, null);

        ResultActions walmartRewardResult = mockMvc.perform(post("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walmartReward)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("WALMART-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());

        String walmartRewardJson = walmartRewardResult.andReturn().getResponse().getContentAsString();
        String walmartRewardId = JsonPath.read(walmartRewardJson, "$.rewardId").toString();

        // SHARE SONY REWARD WITH CUSTOMER

        ShareRewardRequest shareRewardRequest = new ShareRewardRequest(customerId, sonyRewardId);
        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shareRewardRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.birthDate").value("1983-11-15T12:00:00"))
                .andExpect(jsonPath("$.rewards").isNotEmpty())
                .andExpect(jsonPath("$.rewards[0]").value(sonyRewardId));

        // SHARE SONY REWARD WITH NOT EXISTS CUSTOMER

        ShareRewardRequest shareRewardRequest2 = new ShareRewardRequest(sonyRewardId, sonyRewardId);
        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRewardRequest2)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/share-reward"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));


        // SHARE NOT EXISTS REWARD WITH CUSTOMER

        ShareRewardRequest shareRewardRequest3 = new ShareRewardRequest(customerId, customerId);
        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRewardRequest3)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/share-reward"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));


        // MAKE SOME AUTH REQUESTS

        LocalDate date1 = LocalDate.of(2023, 10, 27);
        LocalTime time1 = LocalTime.of(17, 0);
        LocalDateTime localDateTime1 = LocalDateTime.of(date1, time1);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(customerId,
                        sonyRewardId,
                        sonyMerchantId,
                        1.0,
                        localDateTime1);

        mockMvc.perform(post("/api/management/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));


        TransactionAuthorizationRequest request2 =
                new TransactionAuthorizationRequest(customerId,
                        walmartRewardId,
                        walmartMerchantId,
                        1.0,
                        localDateTime1);

        mockMvc.perform(post("/api/management/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));


        LocalDate date2 = LocalDate.of(2023, 10, 26);
        LocalTime time2 = LocalTime.of(19, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(date2, time2);

        TransactionAuthorizationRequest request3 =
                new TransactionAuthorizationRequest(customerId,
                        sonyRewardId, sonyMerchantId, 1000.0, localDateTime2);

        mockMvc.perform(post("/api/management/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));


        // MAKE SOME AUTH REQUESTS WITH WRONG INPUTS


        TransactionAuthorizationRequest wrongDataRequest_customerId =
                new TransactionAuthorizationRequest(sonyRewardId,
                        sonyRewardId,
                        sonyMerchantId,
                        1.0,
                        localDateTime1);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDataRequest_customerId)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));

        TransactionAuthorizationRequest wrongDataRequest_rewardId =
                new TransactionAuthorizationRequest(customerId,
                        customerId,
                        sonyMerchantId,
                        1.0,
                        localDateTime1);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDataRequest_rewardId)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));

        TransactionAuthorizationRequest wrongDataRequest_merchantId =
                new TransactionAuthorizationRequest(customerId,
                        sonyRewardId,
                        customerId,
                        1.0,
                        localDateTime1);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDataRequest_merchantId)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));

    }
}
