# Hermis

<img src="./logo/logo.png" alt="Hermis Logo" width="200"/>

## Overview
**Hermis** is a modern, high-performance messaging platform engineered for reliability and security. Built on Java 21 and Spring Boot 3, it provides a robust, extensible foundation for secure communication across distributed systems.

## Core Intentions
Our primary mission is to enable **secure messaging**. Every architectural decision in Hermis prioritizes data privacy, integrity, and protection against common attack vectors. We aim to deliver a platform where secure transmission is not an afterthought, but the baseline.

## Basic Version Functionalities
- Secure message routing and delivery mechanisms
- Platform-agnostic connectivity via abstract `MessagePlatform` base class
- Resilience patterns: Configurable retry policies with exponential backoff & Circuit Breakers
- Observability: Integrated metrics, latency tracking, and failure counters via `PlatformMetrics`
- Configuration-driven setup using `PlatformConfig`

## Future Roadmap
- End-to-end encryption (E2EE) support for all message types
- Multi-protocol adapters (SMS, Email, Webhooks, XMPP, etc.)
- Advanced threat detection & anomaly monitoring
- Distributed message queue integration (Kafka/RabbitMQ)
- Admin dashboard for real-time platform health, metrics visualization, and credential management

## Usage
1. Clone the repository and configure your target messaging provider credentials via `PlatformConfig`.
2. Initialize and connect the platform:
   ```java
   PlatformConfig config = PlatformConfig.fromUri("your-platform-uri");
   MessagePlatform platform = new YourPlatformImpl(config);
   platform.connect();
   ```
3. Send messages or attachments securely:
   ```java
   Attachment attachment = new Attachment(url, filename, mimeType, size);
   platform.sendMessage(attachment);
   ```
4. Monitor resilience states and metrics through the integrated observability layer.

## Architecture Highlights
- **Security First:** Designed from the ground up to prioritize secure data handling, credential management, and transmission.
- **Resilient by Default:** Built-in retry mechanisms and circuit breakers prevent cascading failures in unstable networks.
- **Extensible:** Abstract base classes and configuration-driven design allow seamless integration with new messaging providers.
- **Modern Stack:** Leverages Java 21 features and Spring Boot 3 standards for performance, maintainability, and developer experience.
