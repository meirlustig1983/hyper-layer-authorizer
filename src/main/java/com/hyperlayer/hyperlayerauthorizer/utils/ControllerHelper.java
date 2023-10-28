package com.hyperlayer.hyperlayerauthorizer.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class ControllerHelper {
    public static URI getLocation() {
        try {
            return new URI("/api/v1/bank-accounts");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
