package com.hermis.platforms.slack;

import com.hermis.core.*;
import com.hermis.enumeration.ChannelTypeE;
import com.hermis.enumeration.PlatformTypeE;
import com.hermis.exception.PlatformException;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.api.ApiTestResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class SlackPlatform extends MessagePlatform {

    private Slack slack;
    private static String TOKEN;
    private MethodsClient methods;

    public SlackPlatform(PlatformConfig config) {
        super();
        configure(config);
        try {
            readProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connect() throws PlatformException {
        try {
            slack = Slack.getInstance();
            ApiTestResponse response = slack.methods().apiTest(r -> r.foo("bar"));
            methods = slack.methods(TOKEN);
            System.out.println(response);
        } catch (IOException | SlackApiException ex) {
            throw new PlatformException(ex);
        }
    }

    @Override
    public void disconnect() {
        try {
            slack.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() { return slack != null; }

    @Override
    public void readProperties() throws IOException {
        final var rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final var appConfigPath = rootPath + "application.properties";

        final var appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        TOKEN = appProps.getProperty("slack.token");
    }

    @Override
    public String send(Message message) throws PlatformException {
        return send(message, "general");
    }

    @Override
    public String send(Message message, String channel) throws PlatformException {
//        String channel = "#general"; // Change to your target channel name or ID
        final var targetChannel = findChannelByName(channel);
        try {
            ChatPostMessageResponse response = methods.chatPostMessage(req -> req
                    .channel(targetChannel.name())
                    .text(message.getContent())
            );

            if (response.isOk()) {
                System.out.println("Message sent successfully! Timestamp: " + response.getTs());
            } else {
                System.err.println("Failed to send message: " + response.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override public List<Message> fetchMessages(String channelId, int limit) throws PlatformException { return null; }
    @Override public List<Channel> listChannels() throws PlatformException { return null; }
    @Override public Channel getChannel(String channelId) throws PlatformException { return null; }

    @Override
    protected Channel findChannelByName(String name) throws PlatformException {
        Optional<Conversation> conversationOptional;
        try {
            // TODO: Check with slack if we can have more that one channel with the same name (especially if the channel is private)
            final var response = methods.conversationsList(req -> req.excludeArchived(true).types(List.of(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL)));
            conversationOptional = response.getChannels().stream().filter(c -> c.getNameNormalized().equals(name)).findFirst();
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }

        if (conversationOptional.isEmpty()) {
            throw new PlatformException("Could not find channel '%s'".formatted(name));
        }
        final var targetChannel = conversationOptional.get();
        return new Channel(targetChannel.getId(), targetChannel.getNameNormalized(), targetChannel.isPrivate() ? ChannelTypeE.PRIVATE.getType() : ChannelTypeE.PUBLIC.getType(), PlatformTypeE.SLACK.getType());
    }

    @Override
    public Channel createChannel(String name, boolean isPrivate) throws PlatformException {
        Channel existingChannel = null;
        try {
            existingChannel = findChannelByName(name);
        } catch (Exception ex) {
            //TODO: An exception does not always mean that the channel does not exists
            System.out.printf("Channel '%s' does not exists, start it's creation%n", name);
        }

        if (existingChannel != null) {
            throw new PlatformException("Channel '%s' already exists".formatted(name));
        }

        try {
            // TODO: Add method to ensure compatible name for the channel (lower case and no special characters https://community.make.com/t/slack-creating-new-channel-error-invalid-name-specials-200/22355)
            final var response = methods.conversationsCreate(req -> req.isPrivate(isPrivate).name(name));
            // TODO: Handle error code
            if (response.isOk()) {
                final var channel = response.getChannel();
                addMembersToChannel(List.of("pantelis.ierissos@gmail.com"), channel.getNameNormalized());
                return new Channel(channel.getId(), channel.getNameNormalized(), channel.isPrivate() ? ChannelTypeE.PRIVATE.getType() : ChannelTypeE.PUBLIC.getType(), PlatformTypeE.SLACK.getType());
            } else {
                throw new PlatformException("Creation of channel '%s' failed: %s".formatted(name, response.getError()));
            }
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMembersToChannel(List<String> users, String channel) throws PlatformException {
        try {
            // TODO: Add logic to be able to filter by email, name , profile etc
           final var selected = methods.usersList( req -> req).getMembers().stream().filter(u -> users.contains(u.getName()) || users.contains(u.getRealName()) || (u.getProfile() != null && u.getProfile().getEmail() != null && users.contains(u.getProfile().getEmail()))).toList();
           final var actualChannel = findChannelByName(channel);
           // TODO: We can add max 30 users in one request, break it to multiple requests if we have more than that
          final var resp = methods.conversationsInvite(req -> req.channel(actualChannel.id()).users(selected.stream().map(u -> u.getId()).toList()));
          resp.isOk();
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void onMessageReceived(MessageHandler handler) {  }
    @Override public String getPlatformName() { return PlatformTypeE.SLACK.getType(); }
}
