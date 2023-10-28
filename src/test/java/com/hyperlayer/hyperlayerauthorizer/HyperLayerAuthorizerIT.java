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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HyperLayerAuthorizerIT {

    private static String CUSTOMER_ID;
    private final static String CUSTOMER_ID_NOT_EXISTS = ObjectId.get().toHexString();
    private static String FIRST_MERCHANT_ID;
    private static String SECOND_MERCHANT_ID;
    private final static String MERCHANT_ID_NOT_EXISTS = ObjectId.get().toHexString();
    private static String FIRST_REWARD_ID;
    private static String SECOND_REWARD_ID;
    private final static String REWARD_ID_NOT_EXISTS = ObjectId.get().toHexString();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createCustomer() throws Exception {

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
        CUSTOMER_ID = JsonPath.read(customersJson, "$.customerId").toString();
    }

    @Test
    @Order(2)
    void createFirstMerchant() throws Exception {

        Merchant sony = new Merchant(null, "Sony");
        ResultActions sonyResult = mockMvc.perform(post("/api/merchants")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sony)))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.merchantId").isNotEmpty())
                .andExpect(jsonPath("$.merchantName").value("Sony"));

        String sonyJson = sonyResult.andReturn().getResponse().getContentAsString();
        FIRST_MERCHANT_ID = JsonPath.read(sonyJson, "$.merchantId").toString();
    }

    @Test
    @Order(3)
    void createSecondMerchant() throws Exception {

        Merchant walmart = new Merchant(null, "Walmart");
        ResultActions walmartResult = mockMvc.perform(post("/api/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walmart)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.merchantId").isNotEmpty())
                .andExpect(jsonPath("$.merchantName").value("Walmart"));

        String walmartJson = walmartResult.andReturn().getResponse().getContentAsString();
        SECOND_MERCHANT_ID = JsonPath.read(walmartJson, "$.merchantId").toString();
    }

    @Test
    @Order(4)
    void createFirstReward() throws Exception {

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
        FIRST_REWARD_ID = JsonPath.read(sonyRewardJson, "$.rewardId").toString();
    }

    @Test
    @Order(5)
    void addRule_firstReward_allowedDaysEvaluator() throws Exception {

        ListRuleRequest allowedDaysList = new ListRuleRequest("AllowedDaysEvaluator",
                List.of("MONDAY", "FRIDAY"));

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/list", FIRST_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allowedDaysList)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());
    }

    @Test
    @Order(6)
    void addRule_firstReward_allowedHoursEvaluator() throws Exception {

        RangeValuesRuleRequest rangeValuesRuleRequest =
                new RangeValuesRuleRequest("AllowedHoursEvaluator",
                        "12 PM", "6 PM");

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/range", FIRST_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rangeValuesRuleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());
    }

    @Test
    @Order(7)
    void addRule_firstReward_allowedMerchantsEvaluator() throws Exception {

        ListRuleRequest allowedMerchantList =
                new ListRuleRequest("AllowedMerchantsEvaluator", List.of(FIRST_MERCHANT_ID));

        mockMvc.perform(patch("/api/rewards/{rewardId}/rules/list", FIRST_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allowedMerchantList)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rewardId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("SONY-001"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.rules").isArray());
    }

    @Test
    @Order(8)
    void createSecondReward() throws Exception {

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
        SECOND_REWARD_ID = JsonPath.read(walmartRewardJson, "$.rewardId").toString();
    }

    @Test
    @Order(9)
    void shareReward() throws Exception {

        ShareRewardRequest shareRewardRequest = new ShareRewardRequest(CUSTOMER_ID, FIRST_REWARD_ID);

        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRewardRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.birthDate").value("1983-11-15T12:00:00"))
                .andExpect(jsonPath("$.rewards").isNotEmpty())
                .andExpect(jsonPath("$.rewards[0]").value(FIRST_REWARD_ID));
    }

    @Test
    @Order(10)
    void shareReward_notExistsCustomer() throws Exception {

        ShareRewardRequest shareRewardRequest2 = new ShareRewardRequest(CUSTOMER_ID_NOT_EXISTS, FIRST_REWARD_ID);

        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRewardRequest2)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/share-reward"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    @Order(11)
    void shareReward_notExistsReward() throws Exception {

        ShareRewardRequest shareRewardRequest2 = new ShareRewardRequest(CUSTOMER_ID, REWARD_ID_NOT_EXISTS);

        mockMvc.perform(post("/api/management/share-reward").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRewardRequest2)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/share-reward"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    @Order(12)
    void authorize_true() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        FIRST_REWARD_ID,
                        FIRST_MERCHANT_ID,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    @Test
    @Order(13)
    void authorize_wrongReward_false() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        SECOND_REWARD_ID,
                        FIRST_MERCHANT_ID,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }

    @Test
    @Order(14)
    void authorize_wrongMerchant_false() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        FIRST_REWARD_ID,
                        SECOND_MERCHANT_ID,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }

    @Test
    @Order(15)
    void authorize_wrongAmount_false() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        FIRST_REWARD_ID,
                        FIRST_MERCHANT_ID,
                        100000000.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }

    @Test
    @Order(16)
    void authorize_wrongCustomerId() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID_NOT_EXISTS,
                        FIRST_REWARD_ID,
                        FIRST_MERCHANT_ID,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    @Order(17)
    void authorize_wrongRewardId() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        REWARD_ID_NOT_EXISTS,
                        FIRST_MERCHANT_ID,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    @Test
    @Order(18)
    void authorize_wrongMerchantId() throws Exception {

        LocalDate date = LocalDate.of(2023, 10, 27);
        LocalTime time = LocalTime.of(17, 0);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        TransactionAuthorizationRequest request1 =
                new TransactionAuthorizationRequest(CUSTOMER_ID,
                        FIRST_REWARD_ID,
                        MERCHANT_ID_NOT_EXISTS,
                        1.0,
                        localDateTime);

        mockMvc.perform(post("/api/management/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/management/authorize"))
                .andExpect(jsonPath("$.message").value("failed to achieve data with input arguments"))
                .andExpect(jsonPath("$.statusCode").value(500));
    }
}
