package com.hyperlayer.hyperlayerauthorizer.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class ControllerHelper {
    public static URI getLocation(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
