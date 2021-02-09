package com.example.jucdemo.cyclicbarrier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CyclicBarrierConsumer {
    private ExecutorService consumeExecutor = Executors.newFixedThreadPool(6);

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

    public void handleMsg(String msg, CyclicBarrier cyclicBarrier){
        consumeExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
                log.info("子线程{}正在处理消息：{},歇会",Thread.currentThread().getName(),msg);
                cyclicBarrier.await();
                log.info("子线程{}处理消息完毕：{}",Thread.currentThread().getName(),msg);

                log.info("子线程{}正在处理消息2：{},歇会",Thread.currentThread().getName(),msg);
                cyclicBarrier.await();
                log.info("子线程{}处理消息完毕2：{}",Thread.currentThread().getName(),msg);
            } catch (BrokenBarrierException | InterruptedException e) {
                log.error("errors,e:{}",e);
            }
        });
    }

    @SneakyThrows
    public static void main(String[] args) {
        List<String> msgList = Arrays.asList("test1","test2","test3");
        CyclicBarrier cb = new CyclicBarrier(3, () -> log.info("BarrierAction,{},执行后续操作。",Thread.currentThread().getName()));
        CyclicBarrierConsumer consumer = new CyclicBarrierConsumer();
        msgList.parallelStream().forEach(t->{
            consumer.handleMsg(t,cb);
        });

        log.info("线程{}执行完毕",Thread.currentThread().getName());
        consumer.doClose();
    }

}
