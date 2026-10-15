package com.messageplatform.platforms.discord;

import com.messageplatform.core.MessagePlatform;

public class DiscordPlatform extends MessagePlatform {
    @Override public void connect() throws PlatformException { ... }
    @Override public void disconnect() { ... }
    @Override public boolean isConnected() { return false; }
    @Override public String send(com.messageplatform.core.Message message) throws PlatformException { return null; }
    @Override public java.util.List<com.messageplatform.core.Message> fetchMessages(String channelId, int limit) throws PlatformException { return null; }
    @Override public java.util.List<com.messageplatform.core.Channel> listChannels() throws PlatformException { return null; }
    @Override public com.messageplatform.core.Channel getChannel(String channelId) throws PlatformException { return null; }
    @Override public void onMessageReceived(com.messageplatform.core.MessageHandler handler) { ... }
    @Override public String getPlatformName() { return "discord"; }
}
