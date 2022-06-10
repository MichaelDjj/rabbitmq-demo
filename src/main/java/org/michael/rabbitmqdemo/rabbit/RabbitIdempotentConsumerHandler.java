package org.michael.rabbitmqdemo.rabbit;

import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

import static org.springframework.amqp.rabbit.connection.PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY;

/**
 * RabbitMQ幂等消费接口处理器
 *
 * @author Dijunjie
 * @date 2022/6/10-18:31
 */
@Component
@Slf4j
@Getter
public class RabbitIdempotentConsumerHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private RedissonClient redissonClient;

    public void handle(Object msgPayload, Channel channel, Message message, Consumer<Object> consumer) throws IOException {
        //保证消息消费幂等性
        //1.获取消息id
        MessageProperties messageProperties = message.getMessageProperties();
        //confirm message id
        String messageId = ((String) messageProperties.getHeaders().get(RETURNED_MESSAGE_CORRELATION_KEY));
        //2.redis加分布式锁，通过setnx原子性加锁
        String key = "mq:lock:" + messageId;
//        RLock lock = redissonClient.getLock(key);
        //redis存入标识位，key=前缀+消息id，value:0-正在处理中，1-处理完成, 过期时间=10s
        Boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(key, "0");
//        Boolean locked = lock.tryLock();
        if (Objects.equals(locked, Boolean.TRUE)) {
            //3.加锁成功消费消息
            try {
                //消费消息
                consumer.accept(msgPayload);
                //手动ack确认消息消费成功，保证消息不会丢失
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info("消息消费成功");
                //4.消费完成，更新redis锁标识
                stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
            } catch (Exception ex) {
                //处理失败
                //利用手动 ACK 和 retry 机制解决了消息丢失的问题
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                //TODO:需要绑定死信队列，失败后消息丢入死信队列，防止消息丢失, 或者存入消费消息表
                log.info("消息消费失败");
                //4.1 消费失败，删除redis锁标识，下次冲入队列可以再次消费
                stringRedisTemplate.delete(key);
//            } finally {
//                //解锁
//                lock.unlock();
            }
        } else {
            //3.1 加锁失败，说明有其他消费者正在消费，不能重复消费，退出
            return;
        }
    }

}
