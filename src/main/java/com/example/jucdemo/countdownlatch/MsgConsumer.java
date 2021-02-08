package com.example.jucdemo.countdownlatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsgConsumer {
    private ExecutorService consumeExecutor = Executors.newFixedThreadPool(5);

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

    public void handleMsg(String msg, CountDownLatch countDownLatch){
        consumeExecutor.execute(() -> {
            log.info("子线程{}正在处理消息：{}",Thread.currentThread().getName(),msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        });
    }

    public static void main(String[] args) {
        List<String> msgList = Arrays.asList("test1","test2","test3");
        CountDownLatch countDownLatch = new CountDownLatch(msgList.size());
        MsgConsumer consumer = new MsgConsumer();
        msgList.parallelStream().forEach(t->{
            consumer.handleMsg(t,countDownLatch);
        });
        log.info("Main Thread wait");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch exception ", e);
        }
        log.info("线程{}加载完毕执行主线程...............",Thread.currentThread().getName());
        consumer.doClose();
    }

}
