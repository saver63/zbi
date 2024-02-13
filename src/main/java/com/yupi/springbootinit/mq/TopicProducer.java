package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class TopicProducer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String userInput = scanner.nextLine();
            String[] strings = userInput.split(" ");
            if (strings.length < 1){
                continue;
            }
            String message = strings[0];
            String routeKey = strings[1];
            //参数：1.交换机的名称，2.路由规则
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "with routing" + routeKey + "'");
        }

    }
  }

}