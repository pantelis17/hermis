package com.hermis.resilience;

public class RateLimiter {
    private int maxRequestsPerSecond;
    
    public boolean tryAcquire() { return true; }
    public void waitForRetryAfter(long seconds) { }
}
