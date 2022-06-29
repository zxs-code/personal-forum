package com.github.code.zxs.core.config;

import com.github.code.zxs.core.support.recoverer.DeadLetterRecoverer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.RetryingBatchErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class KafkaConfig {

    @Autowired
    private ConsumerFactory defaultFactory;

    @Autowired
    private KafkaTemplate template;

    /**
     * 手动提交，达到最大重试次数后，进入死信队列
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory manualFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        factory.setConsumerFactory(defaultFactory);
        Map<String, Object> configProps = new HashMap<>(defaultFactory.getConfigurationProperties());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));

        // 最大重试次数2次，失败进入死信队列
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterRecoverer(template), new FixedBackOff(0L, 2)));
        // 每消费一次，就提交一次offset
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * 批量消费，自动提交offset，重试失败后仅打印错误日志，单次消费上限为500
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory batchFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();

        Map<String, Object> configProps = new HashMap<>(defaultFactory.getConfigurationProperties());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //一次拉取1000条消息
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        //consumer两次poll的最大时间间隔 ms
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int)TimeUnit.MINUTES.toMillis(10));
//        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
        //开启批量消费
        factory.setBatchListener(true);
//        factory.setMessageConverter(new BatchMessagingMessageConverter(converter()));
        // 失败后重试1次，重试失败后打印日志
        factory.setBatchErrorHandler(new RetryingBatchErrorHandler(new FixedBackOff(1000L, 1L), null));
        return factory;
    }

    /**
     * 批量消费，自动提交offset，重试失败后仅打印错误日志，单次消费上限为500
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory manualBatchFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();

        Map<String, Object> configProps = new HashMap<>(defaultFactory.getConfigurationProperties());
        //关闭自动提交
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) TimeUnit.MINUTES.toMillis(10));

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
        //开启批量消费
        factory.setBatchListener(true);
//        factory.setMessageConverter(new BatchMessagingMessageConverter(converter()));
        // 失败后重试1次，重试失败后进入死信队列
        factory.setBatchErrorHandler(new RetryingBatchErrorHandler(new FixedBackOff(0L, 1L), new DeadLetterRecoverer(template)));
        // 每消费一次，就提交一次offset
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * canal
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory canalManualBatchFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();

        Map<String, Object> configProps = new HashMap<>(defaultFactory.getConfigurationProperties());
        //关闭自动提交
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) TimeUnit.MINUTES.toMillis(10));

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProps));
        //开启批量消费
        factory.setBatchListener(true);
//        factory.setMessageConverter(new BatchMessagingMessageConverter(converter()));
        // 失败后重试1次，重试失败后进入死信队列
        factory.setBatchErrorHandler(new RetryingBatchErrorHandler(new FixedBackOff(0L, 1L), new DeadLetterRecoverer(template)));
        // 每消费一次，就提交一次offset
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

}