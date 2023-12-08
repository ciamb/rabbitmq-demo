package com.ciamb.rubbitmqdemo.routingdemo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DirectEmit {

    private static final String LOCALHOST = "localhost";
    private static final String EXCHANGE_NAME = "direct_cr7";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LOCALHOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            var list = Arrays.asList("INFO", "ERROR", "DEBUG");

            for (String severity : list) {
                String argv = "greetings from ciamb with severity : [ " + severity + " ]";
                var message = String.join(" ", argv);

                channel.basicPublish(EXCHANGE_NAME,
                        severity,
                        null,
                        message.getBytes(StandardCharsets.UTF_8)
                );
                System.out.println(" [x] sent ' " + severity + " : { " + message + " } '");
            }

        } catch (Exception e) {
            System.out.println(" unhandled exception; \n" + Arrays.toString(e.getStackTrace()));
        }
    }
}

