package com.messageplatform.service;

import com.messageplatform.core.Message;
import com.messageplatform.core.MessagePlatform;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageRouter {
    private final Map<String, MessagePlatform> platforms = new ConcurrentHashMap<>();

    public void registerPlatform(MessagePlatform platform) { ... }
    public MessagePlatform getPlatform(String name) { ... }
    
    public CompletableFuture<Void> route(Message message, String targetPlatform, String targetChannel) {
        return getPlatform(targetPlatform).sendAsync(message)
                .thenAccept(msgId -> log.info("Routed to {}", msgId))
                .exceptionally(ex -> { /* fallback or alert */ null; });
    }
}
