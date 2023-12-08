package com.ciamb.rubbitmqdemo.topicsdemo;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TopicsReceiver {
    public static final String LOCALHOST = "localhost";
    private static final String EXCHANGE_NAME = "topic";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LOCALHOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        var queueName = channel.queueDeclare().getQueue();

        var keyList = Arrays.asList("*.pigeon", "lazy.#");
        for (var key : keyList) {
            channel.queueBind(queueName, EXCHANGE_NAME, key);
        }

        System.out.println(" [C] Waiting for messagges.. ");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println( " [C] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "' : '" +
                    message + " '");
        };
        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }
}
