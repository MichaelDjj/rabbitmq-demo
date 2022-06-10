package org.michael.rabbitmqdemo.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 增加生成消息确认机制，确保消息可靠投递
 * 消息生产确认分两部分：
 * 1.生产者投递消息给到exchange确认，这个通过confirm机制，yaml中配置spring.rabbitmq.publisher-confirm-type
 * 2.exchange分发消息到queue确认，这个通过return机制，yaml中配置spring.rabbitmq.publisher-returns
 *
 * @author Dijunjie
 * @date 2022/6/10-14:31
 */
@Slf4j
@Configuration
public class RabbitConfirmAndReturnConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback, InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //confirm机制：保证消息生成者投递到exchange
        log.info("消息标识:{}", correlationData);
        //correlationData需要在生产时指定，否则为null
        String messageId = null;
        //消息id，在exchange内全局唯一
        if (Objects.nonNull(correlationData)) {
            messageId = correlationData.getId();
        }
        if (ack) {
            //消息投递成功，回复ack
            log.info("消息投递成功, messageID={}", messageId);
        } else {
            //消息投递失败
            log.error("消息投递到exchange失败，原因={}", cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMsg) {
        //return确认机制，在exchange传递消息到queue的确认机制
        //消息没有从exchange投送到队列queue中
        log.info("消息投递失败，请重新投递消息:{}", returnedMsg);
    }

    @Override
    public void afterPropertiesSet() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnsCallback(this::returnedMessage);
    }
}
