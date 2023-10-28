package com.hyperlayer.hyperlayerauthorizer.exceptions;

public record ApiError(String path, String message, int statusCode) {
}
