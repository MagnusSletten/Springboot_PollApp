package com.example.backend.MessageBrokers;

import org.springframework.stereotype.Component;

import com.example.backend.Model.Vote.VoteDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@Component
public class PollBroker implements PollMessageBroker {

    private static final String EXCHANGE_NAME = "poll";

    public void sendVote(Integer pollId, VoteDTO vote) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        var host = System.getenv().getOrDefault("RABBITMQ_HOST", "rabbitmq");
        var port = Integer.parseInt(System.getenv().getOrDefault("RABBITMQ_PORT", "5672"));

        factory.setHost(System.getenv().getOrDefault("RABBITMQ_HOST", "rabbitmq"));
        factory.setPort(Integer.parseInt(System.getenv().getOrDefault("RABBITMQ_PORT", "5672")));
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            System.out.println("[PollBroker] connecting to " + host + ":" + port);
            String routingKey = "poll." + pollId.toString() + ".vote.cast";
            String message = vote.toJson();

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

        }
    }

    public void recieve(Listener callbackListener) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(System.getenv().getOrDefault("RABBITMQ_HOST", "rabbitmq"));
        factory.setPort(Integer.parseInt(System.getenv().getOrDefault("RABBITMQ_PORT", "5672")));
        ;
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE_NAME, EXCHANGE_NAME + ".*.vote.cast");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                callbackListener.onEvent(message);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}