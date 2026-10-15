package com.messageplatform.service;

import com.messageplatform.core.MessagePlatform;
import com.messageplatform.core.PlatformConfig;

public class PlatformFactory {
    public MessagePlatform create(String platformName, PlatformConfig config) {
        return switch (platformName.toLowerCase()) {
            case "slack"   -> new com.messageplatform.platforms.slack.SlackPlatform(config);
            case "teams"   -> new com.messageplatform.platforms.teams.TeamsPlatform(config);
            case "discord" -> new com.messageplatform.platforms.discord.DiscordPlatform(config);
            case "googlechat" -> new com.messageplatform.platforms.googlechat.GoogleChatPlatform(config);
            case "telegram" -> new com.messageplatform.platforms.telegram.TelegramPlatform(config);
            case "whatsapp" -> new com.messageplatform.platforms.whatsapp.WhatsAppPlatform(config);
            case "matrix"  -> new com.messageplatform.platforms.matrix.MatrixPlatform(config);
            default -> throw new IllegalArgumentException("Unknown platform: " + platformName);
        };
    }
}
