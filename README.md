# HYPER LAYER AUTHORIZER

## 1: Data Bases

The 'Hyper-Layer-Authorizer' is an API-based solution that uses two different databases.

### 1.1: MongoDB (Main DB)

#### 1.1.1: Customer Data

    @Document(collection = "customers")
    public class DbCustomer implements Serializable {

        @Id
        private ObjectId customerId;

        @NonNull
        private String firstName;

        @NonNull
        private String lastName;

        @NonNull
        private LocalDateTime birthDate;

        private Set<ObjectId> rewards;

        @NonNull
        private LocalDateTime createdDate;
    }

#### 1.1.2: Merchant Data

    @Document(collection = "merchants")
    public class DbMerchant implements Serializable {

        @Id
        private ObjectId merchantId;

        @NonNull
        private String merchantName;

        @NonNull
        private LocalDateTime createdDate;
    }

#### 1.1.3: Reward Data

    @Document(collection = "rewards")
    public class DbReward implements Serializable {

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

#### 1.1.3: Transaction Data

    @Document(collection = "transactions")
    public class DbTransaction implements Serializable {

        @Id
        private ObjectId transactionId;

        @NonNull
        private ObjectId customerId;

        @NonNull
        private ObjectId merchantId;

        @NonNull
        private Double amount;
    
        @NonNull
        private LocalDateTime createdDate;
    }

### 1.2: Redis (As cache DB)

### 1.3: Installation

To run the 'Hyper-Layer-Authorizer,' you must either install the required databases or install Docker and use the '[docker-compose.yml](docker-compose.yml)' file.  

## 2: REST API

### 2.1: [Customers API](src%2Fmain%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2Fcontrollers%2FCustomerController.java)

#### 2.1.1: getAllCustomers
    HTTP GET REQUEST ('/api/customers')
#### 2.1.2: getCustomerById
    HTTP GET REQUEST ('/api/customers/{customerId}')
#### 2.1.3: createCustomer
    HTTP POST REQUEST ('/api/customers')
    {
        customerId,
        firstName,
        lastName,
        birthDate,
        rewards
    }
#### 2.1.4: deleteCustomerById 
    HTTP DELETE REQUEST ('/api/customers/{customerId}')

### 2.2: [Merchants API](src%2Fmain%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2Fcontrollers%2FMerchantController.java)

#### 2.2.1: getAllMerchants
    HTTP GET REQUEST ('/api/merchants')
#### 2.2.2: getMerchantById
    HTTP GET REQUEST ('/api/merchants/{merchantId}')
#### 2.2.3: createMerchant
    HTTP POST REQUEST ('/api/merchants')
    {
        merchantId,
        merchantName
    }
#### 2.2.4: deleteMerchantById
    HTTP DELETE REQUEST ('/api/merchants/{merchantId}')

### 2.3: [Transactions API](src%2Fmain%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2Fcontrollers%2FTransactionController.java)

#### 2.3.1: getAllTransaction
    HTTP GET REQUEST ('/api/transactions')
#### 2.3.2: getTransactionsByCustomer
    HTTP GET REQUEST ('/api/transactions/customer/{customerId}')
#### 2.3.3: getTransactionsByCustomerAndMerchant
    HTTP GET REQUEST ('/api/transactions/customer/{customerId}/merchant/{merchantId}')
#### 2.3.4: deleteTransactionsByCustomerId
    HTTP DELETE REQUEST ('/api/transactions/customer/{customerId}')

### 2.4: [Rewards API](src%2Fmain%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2Fcontrollers%2FRewardController.java)

#### 2.4.1: getAllRewards
    HTTP GET REQUEST ('/api/rewards')
#### 2.4.2: getRewardById
    HTTP GET REQUEST ('/api/rewards/{rewardId}')
#### 2.4.3: createReward
    HTTP POST REQUEST ('/api/rewards')
    {
        rewardId,
        name,
        balance,
        rules
    }
#### 2.4.4: addTwoValuesRule
    HTTP PATCH REQUEST ('/api/rewards/{rewardId}/rules/two-values')
    {
        classEvaluator,
        valueA,
        valueB
    }
#### 2.4.5: addListRule
    HTTP PATCH REQUEST ('/api/rewards/{rewardId}/rules/list')
    {
        classEvaluator,
        list
    }
#### 2.4.6: addRangeValuesRule
    HTTP PATCH REQUEST ('/api/rewards/{rewardId}/rules/range')
    {
        classEvaluator,
        start,
        end
    }
#### 2.4.7: deleteRule
    HTTP PATCH REQUEST ('/api/rewards/{rewardId}/rules/{ruleId}')
#### 2.4.8: deleteRewardById
    HTTP DELETE REQUEST ('/api/rewards//{rewardId}')

### 2.4: [Management API](src%2Fmain%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2Fcontrollers%2FManagementController.java)

#### 2.4.1: shareReward
    HTTP POST REQUEST ('/api/management/share-reward')
    {
        customerId,
        rewardId
    }
#### 2.4.2: authorize
    HTTP POST REQUEST ('/api/management/authorize')
    {
        customerId,
        rewardId,
        merchantId,
        amount,
        date
    }

## 3: Integration Tests
### 3.1: [HyperLayerAuthorizerIT](src%2Ftest%2Fjava%2Fcom%2Fhyperlayer%2Fhyperlayerauthorizer%2FHyperLayerAuthorizerIT.java)
To gain a better understanding of how to use this service, it is highly recommended to review the provided examples and tests.

