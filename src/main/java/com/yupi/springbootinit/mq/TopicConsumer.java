package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TopicConsumer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "topic");

      //创建前端队列
      String queueName ="frontend_queue";
      channel.queueDeclare(queueName, true, false, false, null);
      //绑定队列
      channel.queueBind(queueName, EXCHANGE_NAME, "#.前端.#");

      //创建后端队列
      String queueName2 ="backend_queue";
      channel.queueDeclare(queueName2, true, false, false, null);
      //绑定队列
      channel.queueBind(queueName2, EXCHANGE_NAME, "#.后端.#");

      //创建产品队列
      String queueName3 ="product_queue";
      channel.queueDeclare(queueName3, true, false, false, null);
      //绑定队列
      channel.queueBind(queueName3, EXCHANGE_NAME, "#.产品.#");



      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      DeliverCallback xiaoADeliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [xiaozhao] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };
      DeliverCallback xiaoBDeliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [xiaoling] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };

      DeliverCallback xiaoCDeliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [xiaoling] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };


      channel.basicConsume(queueName, true, xiaoADeliverCallback, consumerTag -> { });
      channel.basicConsume(queueName, true, xiaoBDeliverCallback, consumerTag -> { });
      channel.basicConsume(queueName, true, xiaoCDeliverCallback, consumerTag -> { });

  }
}