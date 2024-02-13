package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BiMessageProducer {

    //根据yml配置生成对象
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 消费者发送消息方法
     *
     * @param message 消息
     */
    public void sendMessage( String message){
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, message);
    }
}
