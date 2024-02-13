package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

public class DlxDirectProducer {

  private static final String DEAD_EXCHANGE_NAME = "dlx_direct_exchange";
    private static final String WORK_EXCHANGE_NAME = "direct2_exchange";


    public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, "direct");

        //创建BOSS队列，没有指定队列时随机分配一个队列名称
        String queueName ="BOSS_dlx_queue";
        channel.queueDeclare(queueName, true, false, false, null);
        //绑定队列
        channel.queueBind(queueName, DEAD_EXCHANGE_NAME, "BOSS");



        //创建队列，没有指定队列时随机分配一个队列名称
        String queueName1 ="waibao_dlx_queue";
        channel.queueDeclare(queueName1, true, false, false, null);
        //绑定队列
        channel.queueBind(queueName1, DEAD_EXCHANGE_NAME, "waibao");

        DeliverCallback BOSSADeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaozhao] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback waibaoBDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [xiaoling] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, true, BOSSADeliverCallback, consumerTag -> { });
        channel.basicConsume(queueName, true, waibaoBDeliverCallback, consumerTag -> { });


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
            channel.basicPublish(WORK_EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "with routing" + routeKey + "'");
        }


    }
  }
}