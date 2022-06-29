package com.github.code.zxs.core.support.message;

import java.util.concurrent.ExecutionException;

public interface MessageProducer {

    void syncSend(String topic, String key, Object data) throws Exception;

    void asyncSend(String topic, String key, Object data);

    void syncSend(String topic, Object data) throws ExecutionException, InterruptedException;

    void asyncSend(String topic, Object data);

}
