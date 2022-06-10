package org.michael.rabbitmqdemo.rabbit;

import java.lang.annotation.*;

/**
 * RabbitMQ消费端幂等注解
 * 有此注解后，消费接口实现幂等性，通过消息id全局加锁，每一个消费id接口只能同一时间阻塞消费
 *
 * @author Dijunjie
 * @date 2022/6/10-16:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitIdempotentConsume {

    /**
     * redis同步锁过期时间，单位s，默认10
     */
    int expire() default 10;

}
