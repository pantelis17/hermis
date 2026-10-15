# Message Platform — Design Document (v2.0)

## Overview

`message-platform` is a lightweight, production-ready Java library that acts as a **unified intermediate layer** between a client application and multiple messaging systems (Slack, MS Teams, Discord, Google Chat, Telegram, WhatsApp, Matrix/Zulip/Mattermost, etc.). It abstracts platform-specific APIs behind a single, consistent interface while enforcing resilience, observability, and modern async patterns.

### Design Principles

- **Zero Heavy Dependencies** — No Spring, no Quarkus, no external HTTP clients or JSON parsers. Uses native `java.net.http.HttpClient` (Java 11+) with built-in connection pooling and GZIP support.
- **URI-Based Configuration** — All platforms are configured via standardized URI strings (`slack://bot-token/channel-id`, `discord://webhook-id/webhook-token`, etc.), enabling trivial externalization via environment variables or config servers.
- **Resilience-First** — Built-in rate-limit handling (HTTP 429), exponential backoff, and circuit breakers prevent cascading failures and thread exhaustion.
- **Async & Reactive Native** — Synchronous API by default, but fully supports `CompletableFuture` and optional Project Reactor/RxJava wrappers for non-blocking architectures.
- **Observability Out-of-the-Box** — Native Micrometer integration exposes metrics (`messages.sent`, `messages.failed`, `latency.ms`) without boilerplate.
- **Rich Message Abstraction** — Unified builder pattern automatically translates Markdown, cards, embeds, and attachments into native payloads (Block Kit, Adaptive Cards, etc.).

---

## Package Structure

```
com.messageplatform
├── core/                  # Core abstractions & models
│   ├── Message.java
│   ├── Attachment.java
│   ├── Channel.java
│   ├── MessagePlatform.java       (abstract parent class)
│   ├── MessageHandler.java        (callback interface)
│   └── PlatformConfig.java
├── config/                # URI parsing & configuration
│   ├── UriConfigParser.java
│   └── PlatformCredentials.java
├── resilience/            # Delivery guarantees
│   ├── RetryPolicy.java
│   ├── CircuitBreaker.java
│   └── RateLimiter.java
├── observability/         # Metrics & tracing
│   ├── PlatformMetrics.java
│   └── MicrometerBridge.java
├── formatting/            # Rich payload translation
│   ├── MessageBuilder.java
│   ├── MarkdownTranslator.java
│   └── CardRenderer.java
├── platforms/             # Concrete platform adapters
│   ├── slack/
│   │   └── SlackPlatform.java
│   ├── teams/
│   │   └── TeamsPlatform.java
│   ├── discord/
│   │   └── DiscordPlatform.java
│   ├── googlechat/
│   │   └── GoogleChatPlatform.java
│   ├── telegram/
│   │   └── TelegramPlatform.java
│   ├── whatsapp/
│   │   └── WhatsAppPlatform.java
│   └── matrix/
│       └── MatrixPlatform.java
└── service/               # Orchestration / routing
    ├── MessageRouter.java
    └── PlatformFactory.java
```

---

## Core Classes — Detailed Design

### 1. `Message` & `MessageBuilder` (core / formatting)

The unified message model includes a fluent builder to handle platform-specific rich formatting automatically. The `Message` class holds immutable fields for ID, sender details, channel target, content, timestamp, metadata, and attachments. It also tracks the intended format type (TEXT, MARKDOWN, CARD, EMBED). The `MessageBuilder` provides chainable methods (`withTitle`, `withBody`, `withColor`, `withButton`, `withAttachment`) that construct the payload and return a ready-to-send `Message`. A separate `MarkdownTranslator` normalizes input syntax to platform-native formatting rules.

### 2. `PlatformConfig` & URI Parsing (core / config)

Replaces raw configuration maps with structured parsing and validation. The `PlatformConfig` class stores the target platform name, a map of credentials, and an enabled flag. It exposes a static factory method `fromUri(String)` that parses standardized URI strings into valid configurations. Supported URIs follow the pattern `platform://credential1/credential2?optional_param=value`. A companion `PlatformCredentials` class handles secure storage and validation of tokens, secrets, and endpoints per platform.

### 3. `MessagePlatform` (core) — Abstract Parent Class

This abstract class defines the contract for all messaging adapters. It holds references to the resolved configuration, a native HTTP client instance, resilience policies, and metrics collectors. It enforces lifecycle methods (`connect`, `disconnect`, `isConnected`), sending operations (`send`, `sendAsync`), message retrieval (`fetchMessages`, `fetchMessagesAsync`), channel management (`listChannels`, `getChannel`), event subscription (`onMessageReceived`), and platform identification (`getPlatformName`). Configuration, resilience tuning, and metrics registration are handled via dedicated setters. All network calls route through a resilience wrapper that handles HTTP 429 headers, exponential backoff for 5xx errors, and circuit breaking on consecutive failures. Metrics are automatically incremented per call using Micrometer.

### 4. `MessageHandler` (core) — Interface

A simple functional interface used for push-based delivery. It defines a single method `onMessage(Message)` that is invoked when the platform adapter receives an incoming event or webhook payload.

---

## Resilience & Delivery Guarantees

To prevent production outages caused by third-party API instability:

1. **Rate Limit Handling** — Automatically parses `Retry-After` headers. If exceeded, queues the request or applies dynamic delay based on platform limits.
2. **Exponential Backoff** — Configurable base delay and multiplier (default: 500ms, max 30s). Jitter applied to prevent thundering herd.
3. **Circuit Breakers** — Per-platform state machine (`CLOSED` → `OPEN` → `HALF_OPEN`). Prevents thread blocking when a platform is down. Fallback strategies can be injected.
4. **Connection Pooling** — Native `HttpClient` manages HTTP/2 connections, reducing TLS handshake overhead significantly.

---

## Async & Reactive Support

- **Default:** `CompletableFuture<T>` for fire-and-forget or chaining in standard Java apps.
- **Optional Reactor/RxJava Wrappers:** Provided in a separate module (`message-platform-reactive`) exposing `Mono<MessageResponse>` and `Flux<MessageEvent>` for Spring WebFlux, Quarkus, or custom reactive stacks.
- **Thread Safety:** All platform instances are thread-safe. Handlers can be registered per-platform without synchronization overhead.

---

## Observability & Tracing

- **Metrics:** Native Micrometer integration exposes:
  - `messages.sent{platform="slack"}`
  - `messages.failed{platform="discord",reason="rate_limit"}`
  - `message.latency.ms{platform="teams"}`
- **Distributed Tracing:** Automatic propagation of `TraceContext` (OpenTelemetry compatible) via HTTP headers.
- **Logging:** Structured logging with MDC context (`platform`, `channelId`, `msgId`) for easy log aggregation.

---

## Platform Adapters

Each adapter extends `MessagePlatform` and handles platform-specific HTTP/WebSocket calls, auth flows, and payload translation.

| Platform | API / Protocol | Credentials | Push Mechanism | Formatting Target |
|----------|----------------|-------------|----------------|-------------------|
| **Slack** | Web API + Events API | `bot_token`, `workspace_id` | Socket Mode / Webhook | Block Kit |
| **MS Teams** | Microsoft Graph / Bot Framework | `client_id`, `client_secret`, `tenant_id` | Bot Framework Webhook | Adaptive Cards |
| **Discord** | REST + Gateway | `bot_token`, `guild_id` | Gateway WebSocket | Markdown + Embeds |
| **Google Chat** | Chat API | `api_key` / OAuth2 | Webhook / App Script | Markdown + Cards |
| **Telegram** | Bot API | `bot_token`, `chat_id` | Long Polling / Webhook | MarkdownV2 / HTML |
| **WhatsApp** | Cloud API / Twilio | `phone_number_id`, `access_token` | Webhook | Template Messages / Media |
| **Matrix/Zulip/Mattermost** | REST + WS | `server_url`, `user_id`, `token` | Server-Sent Events / WebSocket | Markdown / HTML |

---

## Service Layer

### `MessageRouter`

Manages a registry of active platform instances and routes messages to the correct adapter. It supports async routing with built-in resilience fallbacks. The router holds a concurrent map of platform names to their respective `MessagePlatform` implementations, provides registration and lookup methods, and exposes a `route` method that delegates to the target platform's async send operation while capturing success/failure outcomes.

### `PlatformFactory`

Responsible for instantiating platform adapters based on configuration. It maps platform identifiers (e.g., "slack", "discord") to their concrete implementations using a switch expression. It validates configurations before instantiation and throws descriptive exceptions for unsupported platforms or malformed credentials.

---

## Nice-to-Have Features & Production Readiness

To ensure the framework is fully production-ready and gives companies complete control over messaging platforms, the following features should be considered for future iterations or as optional modules:

- **Advanced Authentication Management** — Support for OAuth2 flows, token refresh mechanisms, certificate-based auth, and secure credential storage (e.g., HashiCorp Vault integration).
- **Comprehensive Channel Management** — APIs to create, archive, rename, list, and manage permissions for channels/groups across all supported platforms. Includes webhook registration and bot installation wizards.
- **Message History & Sync** — Bidirectional sync capabilities, message archival, search functionality, and pagination support for retrieving historical conversations.
- **Rich Media & File Handling** — Secure upload/download pipelines, virus scanning hooks, format conversion, and CDN integration for large attachments.
- **Multi-Tenant & Workspace Isolation** — Built-in support for managing multiple organizations/workspaces with isolated configurations, rate limits, and metrics.
- **Scheduled & Batch Messaging** — Delayed execution queues, cron-like scheduling, and batch processing with delivery receipts and audit logs.
- **Webhook & Event Management** — Centralized webhook registration, signature verification, retry policies for event delivery, and event routing/filtering.
- **Configuration Validation & Hot-Reloading** — Runtime validation of platform configs, dynamic updates without service restarts, and drift detection.

---

## Future Extensions (out of scope for v1)

- **Message editing / deletion** — `editMessage(String msgId, String newContent)`, `deleteMessage(String msgId)`
- **Reactions / emojis** — `addReaction(String msgId, String emoji)`
- **Scheduled/Delayed Messages** — Internal delay queue with platform-aware scheduling
- **Multi-threaded Event Processing** — Configurable thread pool for `onMessageReceived` handlers
- **Platform-specific Rich Components** — Sliders, date pickers, multi-select dropdowns (where supported)

- Kafka connect to read or write messages to kafka
- interactive messages in msteams
- specific format (templates) and possibility for expansion of them
- Replies to conversation
- Emails

