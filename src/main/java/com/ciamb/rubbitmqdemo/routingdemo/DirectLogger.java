package com.ciamb.rubbitmqdemo.routingdemo;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DirectLogger {

    private static final String LOCALHOST = "localhost";
    private static final String EXCHANGE_NAME = "direct_cr7";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LOCALHOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        var queueName = channel.queueDeclare().getQueue();

        var strings = Arrays.asList("ERROR");
        for (String s : strings) {
            channel.queueBind(queueName, EXCHANGE_NAME, s);
        }
        System.out.println(" [*] is waiting for messages");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(" [x] Received '" +
                            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        var autoAck = true; //auto acknowledge
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }
}
