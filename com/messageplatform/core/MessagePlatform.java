package com.messageplatform.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class MessagePlatform {
    protected PlatformConfig config;
    protected final Object httpClient; // java.net.http.HttpClient with pooling
    protected RetryPolicy retryPolicy;
    protected CircuitBreaker circuitBreaker;
    protected PlatformMetrics metrics;

    public abstract void connect() throws PlatformException;
    public abstract void disconnect();
    public abstract boolean isConnected();

    public abstract String send(Message message) throws PlatformException;
    
    public CompletableFuture<String> sendAsync(Message message) {
        return CompletableFuture.supplyAsync(() -> send(message))
                .exceptionally(ex -> throw new CompletionException(resilience.wrap(ex)));
    }

    public abstract List<Message> fetchMessages(String channelId, int limit) throws PlatformException;
    public CompletableFuture<List<Message>> fetchMessagesAsync(String channelId, int limit) { ... }

    public abstract List<Channel> listChannels() throws PlatformException;
    public abstract Channel getChannel(String channelId) throws PlatformException;

    public abstract void onMessageReceived(MessageHandler handler);

    public void configure(PlatformConfig config) { this.config = config; }
    public void setRetryPolicy(RetryPolicy policy) { this.retryPolicy = policy; }
    public void setCircuitBreaker(CircuitBreaker cb) { this.circuitBreaker = cb; }
    
    public void registerMetrics(MeterRegistry registry) { metrics.register(registry); }
    public abstract String getPlatformName();
}
