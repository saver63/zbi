package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class FanoutConsumer {
  private static final String EXCHANGE_NAME = "fanout-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel1 = connection.createChannel();
    Channel channel2 = connection.createChannel();

    //声明交换机
    channel1.exchangeDeclare(EXCHANGE_NAME, "fanout");
    //创建小王队列，没有指定队列时随机分配一个队列名称
    String queueName ="小王的工作队列";
    channel1.queueDeclare(queueName, true, false, false, null);
    //绑定队列
    channel1.queueBind(queueName, EXCHANGE_NAME, "");

    //创建小李队列
    String queueName2 = "小李的工作队列";
    channel2.queueDeclare(queueName2, true, false, false, null);
    //绑定队列
    channel2.queueBind(queueName2, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    //小王小李的处理流程
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [小王] Received '" + message + "'");
    };
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [小李] Received '" + message + "'");
    };
    channel1.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });
    channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}