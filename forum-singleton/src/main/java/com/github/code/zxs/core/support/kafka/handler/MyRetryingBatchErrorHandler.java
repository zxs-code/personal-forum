package com.github.code.zxs.core.support.kafka.handler;

import com.github.code.zxs.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.RetryingBatchErrorHandler;
import org.springframework.lang.Nullable;
import org.springframework.util.backoff.BackOff;

/**
 * 修复转换消息前，出现错误时日志不全的问题
 */
@Slf4j
public class MyRetryingBatchErrorHandler extends RetryingBatchErrorHandler {
    private static final ThreadLocal<String> lastRecordHolder = new ThreadLocal<>();

//    private final BiConsumer<ConsumerRecords<?, ?>, Exception> recoverer;

    public MyRetryingBatchErrorHandler(BackOff backOff, @Nullable ConsumerRecordRecoverer recoverer) {
        super(backOff, recoverer);
//        this.recoverer = (crs, ex) -> {
//            if (recoverer == null) {
//                log.error("消息消费前出现异常，records: {}", crs, ex);
//            } else {
//                crs.spliterator().forEachRemaining(rec -> recoverer.accept(rec, ex));
//            }
//        };
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container) {
        Object[] keys = data.partitions().stream()
                .map(tp -> StringUtils.join(":", tp.partition(), data.records(tp).get(0).offset(), data.records(tp).size()))
                .toArray();
        String key = StringUtils.join(":", keys);
        if (!key.equals(lastRecordHolder.get())) {
            log.error("消息转换失败", thrownException);
            lastRecordHolder.set(key);
        }
    }
}
