package com.messageplatform.core;

@FunctionalInterface
public interface MessageHandler {
    void onMessage(Message message);
}
