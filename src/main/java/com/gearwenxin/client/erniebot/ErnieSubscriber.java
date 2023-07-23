package com.gearwenxin.client.erniebot;

import com.gearwenxin.common.CommonUtils;
import com.gearwenxin.model.Message;
import com.gearwenxin.model.erniebot.ErnieResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;

import java.util.Queue;

import static com.gearwenxin.common.CommonUtils.buildAssistantMessage;

/**
 * @author Ge Mingjia
 * @date 2023/7/20
 */
@Slf4j
public class ErnieSubscriber implements Subscriber<ErnieResponse>, Disposable {

    private final FluxSink<ErnieResponse> emitter;
    private Subscription subscription;
    Queue<Message> messagesHistory;

    private final StringBuilder stringBuilder = new StringBuilder();

    public ErnieSubscriber(FluxSink<ErnieResponse> emitter, Queue<Message> messagesHistory) {
        this.emitter = emitter;
        this.messagesHistory = messagesHistory;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
        log.info("onSubscribe ==========>");
    }

    @Override
    public void onNext(ErnieResponse response) {
        log.info("onNext ==========>");
        String partResult = response.getResult();
        // 消费一条任务
        subscription.request(1);
        if (StringUtils.isNotEmpty(partResult)) {
            // 拼接每一部分的消息
            stringBuilder.append(partResult);
        }
        emitter.next(response);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("onError ==========>");
        emitter.error(throwable);
    }

    @Override
    public void onComplete() {
        log.info("onComplete ==========>");
        String allResult = stringBuilder.toString();
        Message message = buildAssistantMessage(allResult);
        CommonUtils.offerMessage(messagesHistory, message);
        emitter.complete();
    }

    @Override
    public void dispose() {
        log.info("dispose ==========>");
        subscription.cancel();
    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}