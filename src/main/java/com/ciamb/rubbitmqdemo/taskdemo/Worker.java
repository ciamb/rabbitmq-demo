package com.ciamb.rubbitmqdemo.taskdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker {

    private final static String QUEUE_NAME = "Hello"; //first queue
    private final static String TASK_QUEUE = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;

        channel.queueDeclare(TASK_QUEUE, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. You can exit when you want");

        channel.basicQos(1); //accetta solo un messaggio non correttamente gestito da un altro consumer alla volta

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [*] Received '" + message + "'");

            try {
                doWork(message);
            } catch (Exception e) {
                System.out.println("unhandled error: " + e);
            } finally {
                System.out.println(" [*] hello ciamb");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); //conferma del messaggio
            }
        };

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
