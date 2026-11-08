package com.hermis.service;

import com.hermis.core.MessagePlatform;
import com.hermis.core.PlatformConfig;
//import com.hermis.platforms.discord.DiscordPlatform;
//import com.hermis.platforms.googlechat.GoogleChatPlatform;
//import com.hermis.platforms.matrix.MatrixPlatform;
import com.hermis.platforms.slack.SlackPlatform;
//import com.hermis.platforms.teams.TeamsPlatform;
//import com.hermis.platforms.telegram.TelegramPlatform;
//import com.hermis.platforms.whatsapp.WhatsAppPlatform;

public class PlatformFactory {
    public MessagePlatform create(String platformName, PlatformConfig config) {
        return switch (platformName.toLowerCase()) {
            case "slack"   -> new SlackPlatform(config);
//            case "teams"   -> new TeamsPlatform(config);
//            case "discord" -> new DiscordPlatform(config);
//            case "googlechat" -> new GoogleChatPlatform(config);
//            case "telegram" -> new TelegramPlatform(config);
//            case "whatsapp" -> new WhatsAppPlatform(config);
//            case "matrix"  -> new MatrixPlatform(config);
            default -> throw new IllegalArgumentException("Unknown platform: " + platformName);
        };
    }
}
