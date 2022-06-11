package org.michael.rabbitmqdemo.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.michael.rabbitmqdemo.rabbit.RabbitConfig.*;

/**
 * @author Dijunjie
 * @Description TODO
 * @date 2022/6/10-10:59
 */
@Component
@Slf4j
public class DirectConsumer {

    @Autowired
    private RabbitIdempotentConsumerHandler rabbitIdempotentConsumerHandler;


    @RabbitListener(queues = QUEUE01)
    @RabbitHandler
    public void consumer01(@Payload String msg, Channel channel, Message message) throws IOException {
        log.info("routing消费者1号:{}", msg);
        //手动ack确认消息消费成功，保证消息不会丢失
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = QUEUE02)
    @RabbitHandler
    public void consumer02(@Payload String msg, Channel channel, Message message) throws IOException {
        //保证消息消费幂等性
        rabbitIdempotentConsumerHandler.handle(channel, message,
                new CallbackConsumer(msg, o -> {
                    //真正消费逻辑
                    log.info("routing消费者2号:{}", o);
                }));
    }


    @RabbitListener(queues = QUEUE03)
    @RabbitHandler
    public void consumer03(@Payload String msg, Channel channel, Message message) throws IOException {
        //手动ack确认消息消费成功，保证消息不会丢失
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 解决重复消费问题，即确保消息消费的幂等性
     * 解决方案：在消费消息前通过redis加锁，标志为消息的唯一id
     *
     * @param channel
     * @param message
     */
    public void idempotentConsume(Channel channel, Message message) {

    }

}
