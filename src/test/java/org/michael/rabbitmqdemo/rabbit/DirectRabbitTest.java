package org.michael.rabbitmqdemo.rabbit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.michael.rabbitmqdemo.rabbit.RabbitConfig.DIRECT_EXCHANGE;

/**
 * @author Dijunjie
 * @Description TODO
 * @date 2022/6/10-11:18
 */
@SpringBootTest
class DirectRabbitTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    private void testDirect() {
    }

}