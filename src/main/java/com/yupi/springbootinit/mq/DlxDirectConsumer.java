package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxDirectConsumer {
  private static final String DEAD_EXCHANGE_NAME = "dlx_direct_exchange";
  private static final String WORK_EXCHANGE_NAME = "direct2_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");


    //指定死信队列参数
    Map<String, Object> args = new HashMap<>();
    //要绑定到哪个交换机
    args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    //指定死信要转发到哪个队列
    args.put("x-dead-letter-routing-key", "waibao");

    //创建队列，没有指定队列时随机分配一个队列名称
     String queueName ="xiaodog_queue";
    channel.queueDeclare(queueName, true, false, false, args);
    //绑定队列
    channel.queueBind(queueName, WORK_EXCHANGE_NAME, "xiaodog");

    //指定死信队列参数
    Map<String, Object> args2 = new HashMap<>();
    //要绑定到哪个交换机
    args2.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    //指定死信要转发到哪个队列
    args2.put("x-dead-letter-routing-key", "BOSS");


      //创建队列，没有指定队列时随机分配一个队列名称
    String queueName2 ="xiaocat_queue";
    channel.queueDeclare(queueName2, true, false, false, args2);
    //绑定队列
    channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "xiaocat");




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

    channel.basicConsume(queueName, false, xiaozhaoDeliverCallback, consumerTag -> { });
    channel.basicConsume(queueName, false, xiaolingDeliverCallback, consumerTag -> { });
  }
}