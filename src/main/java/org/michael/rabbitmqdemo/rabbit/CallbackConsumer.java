package org.michael.rabbitmqdemo.rabbit;

import java.util.function.Consumer;

/**
 * @author Dijunjie
 * @date 2022/6/11-10:55
 */
public class CallbackConsumer<T> {

    private T object;
    private Consumer<T> consumer;

    public CallbackConsumer(T object, Consumer<T> consumer) {
        this.object = object;
        this.consumer = consumer;
    }

    void exec() {
        consumer.accept(object);
    }

}
