package com.messageplatform.resilience;

public class RateLimiter {
    private final int maxRequestsPerSecond;
    
    public boolean tryAcquire() { ... }
    public void waitForRetryAfter(long seconds) { ... }
}
