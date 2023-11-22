package com.ciamb.rubbitmqdemo.taskdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NewTask {
    private final static String QUEUE_NAME = "Hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String argv = "saluti da ciamb...........";

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            var message = String.join(" ", argv);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] sent '" + message + "'");
        } catch (Exception e) {
            System.out.println("unhandled exception: " + e);
        }
    }
}
