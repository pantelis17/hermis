package com.messageplatform.resilience;

import java.time.Duration;

public class RetryPolicy {
    private final int maxAttempts;
    private final Duration delay;
    private final BackoffStrategy strategy;

    public static RetryPolicy exponentialBackoff(int maxAttempts, Duration baseDelay) { ... }
}
