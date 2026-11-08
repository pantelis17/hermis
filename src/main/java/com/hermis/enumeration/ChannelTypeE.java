package com.hermis.enumeration;

import lombok.Getter;

@Getter
public enum ChannelTypeE {
    PRIVATE("Private"),
    PUBLIC("Public");

    private final String type;

    ChannelTypeE(String type){
        this.type = type;
    }
}
