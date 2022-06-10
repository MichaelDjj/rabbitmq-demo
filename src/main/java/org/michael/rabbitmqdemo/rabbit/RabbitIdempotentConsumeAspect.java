package org.michael.rabbitmqdemo.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Dijunjie
 * @date 2022/6/10-16:21
 */
@Component
@Aspect
@Slf4j
public class RabbitIdempotentConsumeAspect {

    /**
     * rabbit 消费接口，并且有@RabbitIdempotentConsume注解
     */
    @Pointcut("(execution(public * org.michael.rabbitmqdemo.rabbit.*Consumer.*(..)) " +
            "&& (@annotation(org.michael.rabbitmqdemo.rabbit.RabbitIdempotentConsume)))" +
            "")
    void pointcut() {
        // just a Pointcut, no method body
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 参数列表
        Object[] args = joinPoint.getArgs();
        // 方法签名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RabbitIdempotentConsume declaredAnnotation = method.getDeclaredAnnotation(RabbitIdempotentConsume.class);
        if (declaredAnnotation != null) {
            //redis key超时时间
            int expire = declaredAnnotation.expire();

        }
        return joinPoint.proceed(args);
    }

}
