package com.hyperlayer.hyperlayerauthorizer.dto;

import lombok.NonNull;

public record Merchant(String merchantId, @NonNull String merchantName) {
}
