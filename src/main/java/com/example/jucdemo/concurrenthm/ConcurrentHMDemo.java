package com.example.jucdemo.concurrenthm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrentHMDemo {
    private ExecutorService consumeExecutor = Executors.newFixedThreadPool(5);
    private static Map<String,Object> conMap = new ConcurrentHashMap<>();
    private static Map<String,Object> conMap2 = new ConcurrentHashMap<>();
    @PreDestroy
    public void doClose() {
        if (!consumeExecutor.isShutdown()) {
            consumeExecutor.shutdown();
            try {
                // Wait a while for existing tasks to terminate.
                if (!consumeExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    consumeExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // (Re-)Cancel if current thread also interrupted.
                consumeExecutor.shutdownNow();
                // Preserve interrupt status.
                Thread.currentThread().interrupt();
            }
            log.info("consumeExecutor stopped");
        }
    }

    public void handleMsg(Map<String,Object> conMap, CountDownLatch countDownLatch){
        consumeExecutor.execute(() -> {
            log.info("子线程{}正在处理消息：{}",Thread.currentThread().getName());
            while (true) {
                Integer oldValue = (Integer)conMap.get("score");
                if (oldValue == null) {
                    if(conMap.putIfAbsent("score", 1)==null)
                        break;
                } else {
                    if (conMap.replace("score",oldValue,oldValue+1)) {
                        break;
                    }
                }
            }
            countDownLatch.countDown();
        });
    }

    public void handleMsg2(Map<String,Object> conMap, CountDownLatch countDownLatch){
        consumeExecutor.execute(() -> {
            log.info("子线程{}正在处理消息：{}",Thread.currentThread().getName());
            AtomicInteger oldValue = (AtomicInteger)conMap.get("score");
            if (oldValue == null) {
                AtomicInteger newValue = new AtomicInteger(0);
                oldValue = (AtomicInteger)conMap.putIfAbsent("score",newValue);
                if (oldValue == null) {
                    oldValue = newValue;
                }
            }
            oldValue.incrementAndGet();
            countDownLatch.countDown();
        });
    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2000);
        ConcurrentHMDemo consumer = new ConcurrentHMDemo();
        for (int i =0; i<1000; i++){
            consumer.handleMsg2(conMap2,countDownLatch);
            consumer.handleMsg(conMap,countDownLatch);
        }

        log.info("Main Thread wait");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch exception ", e);
        }
        log.info("线程{}加载完毕执行主线程...............",Thread.currentThread().getName());
        System.out.println("conMap:"+conMap.get("score"));
        System.out.println("conMap2:"+conMap2.get("score"));
        consumer.doClose();
    }

}
