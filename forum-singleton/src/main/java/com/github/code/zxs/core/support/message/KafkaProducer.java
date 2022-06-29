package com.github.code.zxs.core.support.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@ConditionalOnProperty(prefix = "bbs.core.message", name = "instance", havingValue = "kafka")
public class KafkaProducer implements MessageProducer {
    @SuppressWarnings("all")
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void syncSend(String topic, String key, Object data) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(topic, key, data).get();
    }

    @Override
    public void asyncSend(String topic, String key, Object data) {
        kafkaTemplate.send(topic, key, data);
    }

    @Override
    public void syncSend(String topic, Object data) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(topic, data).get();
    }

    @Override
    public void asyncSend(String topic, Object data) {
        kafkaTemplate.send(topic, data);
    }


}
