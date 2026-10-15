package com.messageplatform.config;

import java.util.Map;

public class PlatformCredentials {
    private final String platform;
    private final Map<String, String> tokens;
    private final Map<String, String> endpoints;

    public PlatformCredentials(String platform, Map<String, String> tokens, Map<String, String> endpoints) {
        this.platform = platform;
        this.tokens = tokens;
        this.endpoints = endpoints;
    }

    public String getPlatform() { return platform; }
    public Map<String, String> getTokens() { return tokens; }
    public Map<String, String> getEndpoints() { return endpoints; }
}
