package org.michael.rabbitmqdemo.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.michael.rabbitmqdemo.rabbit.RabbitConfig.*;

/**
 * @author Dijunjie
 * @Description TODO
 * @date 2022/6/10-11:26
 */
@Slf4j
@Service
public class RabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void doProduce(String message) {
//        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, "hello", "生产者1号" + message, new CorrelationData());
//        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, ROUTING_KEY01, "生产者2号:" + message, new CorrelationData());
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, ROUTING_KEY02, "生产者3号:" + message, new CorrelationData());
    }
}
