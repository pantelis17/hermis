package com.hermis.core;

@FunctionalInterface
public interface MessageHandler {
    void onMessage(Message message);
}
