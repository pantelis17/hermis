package com.hermis.resilience;

import java.time.Duration;

public class CircuitBreaker {
    private  int failureThreshold;
    private  Duration resetTimeout;
    private State state;

    public enum State { CLOSED, OPEN, HALF_OPEN }
}
