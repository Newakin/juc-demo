package com.example.jucdemo.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhaserConsumer {
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

    public void handleMsg(Phaser myPhaser){
        consumeExecutor.execute(() -> {
            log.info("子线程{}开始Phase0",Thread.currentThread().getName());
            myPhaser.arriveAndAwaitAdvance();

            log.info("子线程{}开始Phase1",Thread.currentThread().getName());
            myPhaser.arriveAndAwaitAdvance();

            log.info("子线程{}开始Phase2",Thread.currentThread().getName());
            myPhaser.arriveAndAwaitAdvance();
        });
    }

    public static void main(String[] args) {
        Phaser myPhaser = new MyPhaser();
        PhaserConsumer consumer = new PhaserConsumer();
        for (int i=0; i<3; i++) {
            myPhaser.register();
            consumer.handleMsg(myPhaser);
        }

        consumer.doClose();
    }

}
