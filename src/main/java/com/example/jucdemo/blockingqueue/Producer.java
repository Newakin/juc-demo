package com.example.jucdemo.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
    protected BlockingQueue queue;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i=0; i<3; i++) {
                queue.put(i);
                System.out.println("生产了一个物品,"+i);
                Thread.sleep(new Random().nextInt(5000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
