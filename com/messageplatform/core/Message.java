package com.messageplatform.core;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Message {
    private final String id;
    private final String senderId;
    private final String senderName;
    private final String channelId;
    private final String content;
    private final Instant timestamp;
    private final Map<String, String> metadata;
    private final List<Attachment> attachments;
    private final String formatType;

    private Message(Builder builder) {
        this.id = builder.id;
        this.senderId = builder.senderId;
        this.senderName = builder.senderName;
        this.channelId = builder.channelId;
        this.content = builder.content;
        this.timestamp = builder.timestamp;
        this.metadata = builder.metadata;
        this.attachments = builder.attachments;
        this.formatType = builder.formatType;
    }

    public static Builder of(String channelId) {
        return new Builder(channelId);
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getChannelId() { return channelId; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, String> getMetadata() { return metadata; }
    public List<Attachment> getAttachments() { return attachments; }
    public String getFormatType() { return formatType; }

    public static class Builder {
        private final String channelId;
        private String id;
        private String senderId;
        private String senderName;
        private String content;
        private Instant timestamp = Instant.now();
        private Map<String, String> metadata = Map.of();
        private List<Attachment> attachments = List.of();
        private String formatType = "TEXT";

        public Builder(String channelId) { this.channelId = channelId; }

        public Builder withId(String id) { this.id = id; return this; }
        public Builder withSenderId(String senderId) { this.senderId = senderId; return this; }
        public Builder withSenderName(String senderName) { this.senderName = senderName; return this; }
        public Builder withContent(String content) { this.content = content; return this; }
        public Builder withTimestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder withMetadata(Map<String, String> metadata) { this.metadata = metadata; return this; }
        public Builder withAttachments(List<Attachment> attachments) { this.attachments = attachments; return this; }
        public Builder withFormatType(String formatType) { this.formatType = formatType; return this; }

        public Message build() { return new Message(this); }
    }
}
