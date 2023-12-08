package com.ciamb.rubbitmqdemo.topicsdemo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TopicsEmitter {
    public static final String LOCALHOST = "localhost";
    private static final String EXCHANGE_NAME = "topic";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LOCALHOST);
        var message = "Greetings from ciamb";

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String rKey = "damn.pigeon";

            channel.basicPublish(EXCHANGE_NAME,
                    rKey,
                    null,
                    message.getBytes(StandardCharsets.UTF_8)
            );
            System.out.println(" [P] Sent ' " + rKey + "' : '" + message + " '");

        } catch (Exception e) {
            System.out.println("unhandled exception; \n" + Arrays.toString(e.getStackTrace()));
        }
    }

}
