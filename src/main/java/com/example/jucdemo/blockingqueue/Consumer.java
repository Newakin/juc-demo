package com.example.jucdemo.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
    protected BlockingQueue queue;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i=0; i<3; i++) {
                System.out.println("消费了一个物品，"+queue.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
