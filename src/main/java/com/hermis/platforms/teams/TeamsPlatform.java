package com.hermis.platforms.teams;

import com.hermis.core.Channel;
import com.hermis.core.Message;
import com.hermis.core.MessageHandler;
import com.hermis.core.MessagePlatform;
import com.hermis.exception.PlatformException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TeamsPlatform extends MessagePlatform {
    @Override public void connect() throws PlatformException {  }
    @Override public void disconnect() {  }
    @Override public boolean isConnected() { return false; }

    @Override
    public void readProperties() throws IOException {

    }

    @Override public String send(Message message) throws PlatformException { return null; }

    @Override
    public String send(Message message, String target) throws PlatformException {
        return "";
    }

    @Override public List<Message> fetchMessages(String channelId, int limit) throws PlatformException { return null; }
    @Override public List<Channel> listChannels() throws PlatformException { return null; }
    @Override public Channel getChannel(String channelId) throws PlatformException { return null; }

    @Override
    protected Channel findChannelByName(String name) throws PlatformException {
        return null;
    }

    @Override
    public Channel createChannel(String name, boolean isPrivate) throws PlatformException {
        return null;
    }

    @Override
    public void addMembersToChannel(List<String> users, String channel) throws PlatformException {

    }

    @Override public void onMessageReceived(MessageHandler handler) {  }
    @Override public String getPlatformName() { return "teams"; }
}
