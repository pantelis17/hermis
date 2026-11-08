package com.hermis.resilience;

import java.time.Duration;

public class RetryPolicy {
    private  int maxAttempts;
    private  Duration delay;
//    private final BackoffStrategy strategy;

    public static RetryPolicy exponentialBackoff(int maxAttempts, Duration baseDelay) { return null; }
}
