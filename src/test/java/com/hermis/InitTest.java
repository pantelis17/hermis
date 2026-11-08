package com.hermis;


import com.hermis.core.Message;
import com.hermis.core.PlatformConfig;
import com.hermis.platforms.slack.SlackPlatform;
import com.hermis.service.PlatformFactory;
import org.junit.jupiter.api.Test;

public class InitTest {

    @Test
    public void mock(){
        PlatformConfig config = PlatformConfig.fromUri("");
        final var factory = new PlatformFactory();
        final var message = factory.create("slack", config);
        System.out.println(message.getPlatformName());

        final var slack = new SlackPlatform(config);
        slack.connect();
        if (slack.isConnected()) {
            System.out.println("yes i am here");
            final var msg = Message.of("random").withContent(":wave: Hello from my Java application!").build();
            slack.send(msg, "general");
        }

    }
}
