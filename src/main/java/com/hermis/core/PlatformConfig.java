package com.hermis.core;

import java.util.Map;

public class PlatformConfig {
    private String platform;
    private final Map<String, String> credentials = new java.util.HashMap<>();
    private boolean enabled;
    
    public static PlatformConfig fromUri(String uri) { return null; }
}
