package com.messageplatform.core;

public class Channel {
    private final String id;
    private final String name;
    private final String type;
    private final String platform;

    public Channel(String id, String name, String type, String platform) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.platform = platform;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getPlatform() { return platform; }
}
