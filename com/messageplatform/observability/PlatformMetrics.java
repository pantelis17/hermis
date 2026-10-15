package com.messageplatform.observability;

import io.micrometer.core.instrument.MeterRegistry;

public class PlatformMetrics {
    private final Counter messagesSent;
    private final Counter messagesFailed;
    private final Timer latency;

    public void register(MeterRegistry registry) { ... }
}
