package org.michael.rabbitmqdemo.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dijunjie
 * @Description TODO
 * @date 2022/6/10-11:01
 */
@Configuration
public class RabbitConfig {

    public static final String DIRECT_EXCHANGE = "amq.direct";

    public static final String QUEUE01 = "hello";
    public static final String QUEUE02 = "nihao";
    public static final String QUEUE03 = "kongnijiwa";

    public static final String ROUTING_KEY01 = "abcd";
    public static final String ROUTING_KEY02 = "info";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Queue queue01() {
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02() {
        return new Queue(QUEUE02);
    }

    @Bean
    public Queue queue03() {
        return new Queue(QUEUE03);
    }

    @Bean
    public Binding directBinding01() {
        return BindingBuilder.bind(queue01())
                .to(directExchange())
                .with(ROUTING_KEY01);
    }
    @Bean
    public Binding directBinding02() {
        return BindingBuilder.bind(queue02())
                .to(directExchange())
                .with(ROUTING_KEY02);
    }
    @Bean
    public Binding directBinding03() {
        return BindingBuilder.bind(queue03())
                .to(directExchange())
                .with(ROUTING_KEY01);
    }

}
