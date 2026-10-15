package com.messageplatform.core;

import java.util.Map;

public class PlatformConfig {
    private final String platform;
    private final Map<String, String> credentials = new java.util.HashMap<>();
    private boolean enabled = true;
    
    public static PlatformConfig fromUri(String uri) { ... }
}
