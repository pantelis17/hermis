package com.hermis.service;

import com.hermis.core.Message;
import com.hermis.core.MessagePlatform;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class MessageRouter {
    private final Map<String, MessagePlatform> platforms = new ConcurrentHashMap<>();

    public void registerPlatform(MessagePlatform platform) {  }
    public MessagePlatform getPlatform(String name) { return null; }
    
    public CompletableFuture<Void> route(Message message, String targetPlatform, String targetChannel) {
        return getPlatform(targetPlatform).sendAsync(message)
                .thenAccept(msgId -> log.info("Routed to " + msgId))
                .exceptionally(ex -> null);
    }
}
