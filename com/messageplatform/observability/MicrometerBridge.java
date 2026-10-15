package com.messageplatform.observability;

import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerBridge {
    public static PlatformMetrics createDefault(MeterRegistry registry) { ... }
}
