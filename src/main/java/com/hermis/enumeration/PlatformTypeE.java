package com.hermis.enumeration;

import lombok.Getter;

@Getter
public enum PlatformTypeE {
    SLACK("Slack"),
    MS_TEAMS("Microsoft Teams"),
    DISCORD("Discord");

    private final String type;

    PlatformTypeE(String type){
        this.type = type;
    }
}
