package com.hermis.core;

import com.hermis.exception.PlatformException;
import com.hermis.observability.PlatformMetrics;
import com.hermis.resilience.CircuitBreaker;
import com.hermis.resilience.RetryPolicy;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class MessagePlatform {
    protected PlatformConfig config;
    protected Object httpClient; // java.net.http.HttpClient with pooling
    protected RetryPolicy retryPolicy;
    protected CircuitBreaker circuitBreaker;
    protected PlatformMetrics metrics;

    public abstract void connect() throws PlatformException;
    public abstract void disconnect();
    public abstract boolean isConnected();
    public abstract void readProperties() throws IOException;

    public abstract String send(Message message) throws PlatformException;
    public abstract String send(Message message, String target) throws PlatformException;
    
    public CompletableFuture<String> sendAsync(Message message) {
        return CompletableFuture.supplyAsync(() -> send(message))
                .exceptionally(ex -> null);
    }

    public abstract List<Message> fetchMessages(String channelId, int limit) throws PlatformException;
    public CompletableFuture<List<Message>> fetchMessagesAsync(String channelId, int limit) { return null; }

    public abstract List<Channel> listChannels() throws PlatformException;
    public abstract Channel getChannel(String channelId) throws PlatformException;
    protected  abstract  Channel findChannelByName(String name) throws PlatformException;
    public abstract Channel createChannel(String name, boolean isPrivate) throws PlatformException;
    public abstract void addMembersToChannel(List<String> users) throws PlatformException;

    public abstract void onMessageReceived(MessageHandler handler);

    public void configure(PlatformConfig config) { this.config = config; }
    public void setRetryPolicy(RetryPolicy policy) { this.retryPolicy = policy; }
    public void setCircuitBreaker(CircuitBreaker cb) { this.circuitBreaker = cb; }
    
//    public void registerMetrics(MeterRegistry registry) { metrics.register(registry); }
    public abstract String getPlatformName();
}
