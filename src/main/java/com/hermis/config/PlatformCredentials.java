package com.hermis.config;

import java.util.Map;

public record PlatformCredentials(String platform, Map<String, String> tokens, Map<String, String> endpoints) {
}
