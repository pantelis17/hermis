package com.messageplatform.formatting;

import com.messageplatform.core.Attachment;
import com.messageplatform.core.Message;

public class MessageBuilder {
    private final String channelId;
    
    public static MessageBuilder of(String channelId) { ... }
    public MessageBuilder withTitle(String title) { return this; }
    public MessageBuilder withBody(String body) { return this; }
    public MessageBuilder withColor(String color) { return this; }
    public MessageBuilder withButton(String label, String url) { return this; }
    public MessageBuilder withAttachment(Attachment attachment) { return this; }
    public Message build() { ... }
}
