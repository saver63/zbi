package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class DirectConsumer {

  private static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");

    //创建小zhao队列，没有指定队列时随机分配一个队列名称
     String queueName ="小zhao的工作队列";
    channel.queueDeclare(queueName, true, false, false, null);
    //绑定队列
    channel.queueBind(queueName, EXCHANGE_NAME, "小zhao");

    //创建小ling队列，没有指定队列时随机分配一个队列名称
     String queueName2 ="小ling的工作队列";
    channel.queueDeclare(queueName2, true, false, false, null);
    //绑定队列
    channel.queueBind(queueName2, EXCHANGE_NAME, "小ling");




    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback xiaozhaoDeliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaozhao] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
      DeliverCallback xiaolingDeliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [xiaoling] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };

    channel.basicConsume(queueName, true, xiaozhaoDeliverCallback, consumerTag -> { });
    channel.basicConsume(queueName, true, xiaolingDeliverCallback, consumerTag -> { });
  }
}