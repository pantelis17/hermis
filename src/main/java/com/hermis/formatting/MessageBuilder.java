package com.hermis.formatting;

import com.hermis.core.Attachment;
import com.hermis.core.Message;

public class MessageBuilder {
    private String channelId;
    
    public static MessageBuilder of(String channelId) { return null;  }
    public MessageBuilder withTitle(String title) { return this; }
    public MessageBuilder withBody(String body) { return this; }
    public MessageBuilder withColor(String color) { return this; }
    public MessageBuilder withButton(String label, String url) { return this; }
    public MessageBuilder withAttachment(Attachment attachment) { return this; }
    public Message build() { return null; }
}
