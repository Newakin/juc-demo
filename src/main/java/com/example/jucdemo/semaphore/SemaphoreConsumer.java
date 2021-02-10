package com.example.jucdemo.semaphore;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SemaphoreConsumer {
    private ExecutorService consumeExecutor = Executors.newFixedThreadPool(10);

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

    public void handleMsg(Semaphore semaphore){
        consumeExecutor.execute(() -> {
            try {
                if (semaphore.availablePermits() == 0) {
                    log.info("{}车位不足，请等待",Thread.currentThread().getName());
                }
                semaphore.acquire();
                log.info("{}成功进入停车场", Thread.currentThread().getName());
                Thread.sleep(new Random().nextInt(5000));
            } catch (InterruptedException e) {
                log.error("errors,e:{}", e);
            } finally{
                log.info("{}离开停车场", Thread.currentThread().getName());
                semaphore.release();
            }
        });
    }

    @SneakyThrows
    public static void main(String[] args) {
        SemaphoreConsumer consumer = new SemaphoreConsumer();
        //停车场就10个车位
        Semaphore semaphore = new Semaphore(2);
        for (int i =0; i<10; i++){
            //有10辆车等着进入停车场
            consumer.handleMsg(semaphore);
        }
    }

}
