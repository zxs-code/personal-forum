package com.github.code.zxs.core.support.kafka.recoverer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;

public class DeadLetterRecoverer extends DeadLetterPublishingRecoverer {

    private static final ThreadLocal<Exception> lastExceptionHolder = new ThreadLocal<>();

    public DeadLetterRecoverer(KafkaOperations<?, ?> template) {
        super(template);
    }

    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception exception) {
        Exception ex = lastExceptionHolder.get();
        if (ex != exception) {
            logger.error(exception, "消费失败消息进入死信队列" + record);
            lastExceptionHolder.set(exception);
        }
        super.accept(record, exception);
        super.accept(record, exception);
    }
}
