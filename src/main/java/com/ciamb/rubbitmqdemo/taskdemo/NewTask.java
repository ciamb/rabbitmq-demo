package com.ciamb.rubbitmqdemo.taskdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
    private final static String QUEUE_NAME = "Hello";
    private final static String TASK_QUEUE = "task_queue"


    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String argv = "hello from italy....................";

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            boolean durable = true;
            channel.queueDeclare(TASK_QUEUE, durable, false, false, null);

            var message = String.join(" ", argv);

            channel.basicPublish("",
                    TASK_QUEUE,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());

            System.out.println(" [x] sent '" + message + "'");
        } catch (Exception e) {
            System.out.println("unhandled exception: " + e);
        }
    }
}
