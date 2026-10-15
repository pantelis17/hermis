package com.messageplatform.resilience;

import java.time.Duration;

public class CircuitBreaker {
    private final int failureThreshold;
    private final Duration resetTimeout;
    private State state;

    public enum State { CLOSED, OPEN, HALF_OPEN }
}
