package com.yryc.imlib.rx;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


public class RxBus {

    private final FlowableProcessor<Object> bus;

    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
        bus.onBackpressureBuffer(Integer.MAX_VALUE);
    }

    public static RxBus getInstance() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    /**
     * 发出事件序列
     *
     * @param object
     */
    public void post(Object object) {
        bus.onNext(object);
    }

    /**
     * 过滤事件序列
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 默认过滤事件序列并消费
     *
     * @param eventType
     * @param consumer
     * @param <T>
     * @return
     */
    public <T> Disposable toDefaultFlowable(Class<T> eventType, Consumer<T> consumer) {
        return bus.ofType(eventType).compose(RxUtils.rxSchedulerHelper()).subscribe(consumer);
    }
}
