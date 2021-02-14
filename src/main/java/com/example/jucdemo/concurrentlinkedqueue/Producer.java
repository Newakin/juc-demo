package com.example.jucdemo.concurrentlinkedqueue;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Producer implements Runnable{
    protected ConcurrentLinkedQueue<Integer> queue;

    public Producer(ConcurrentLinkedQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i=0; i<10; i++) {
                queue.add(i);
                System.out.println("生产了一个物品,"+i);
                Thread.sleep(new Random().nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
