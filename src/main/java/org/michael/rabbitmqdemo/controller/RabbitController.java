package org.michael.rabbitmqdemo.controller;

import org.michael.rabbitmqdemo.rabbit.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dijunjie
 * @Description TODO
 * @date 2022/6/10-12:15
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    private RabbitProducer producer;

    @GetMapping("/produce/{message}")
    public void produce(@PathVariable String message) {
        producer.doProduce(message);
    }
}
