package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyMessageProducer {

    //根据yml配置生成对象
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 消费者发送消息方法
     *
     * @param exchange 交换机
     * @param routeKey 路由键
     * @param message 消息
     */
    public void sendMessage(String exchange, String routeKey, String message){
        rabbitTemplate.convertAndSend(exchange, routeKey, message);
    }
}
