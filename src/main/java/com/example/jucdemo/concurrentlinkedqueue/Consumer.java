package com.example.jucdemo.concurrentlinkedqueue;

import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.SneakyThrows;

public class Consumer implements Runnable{
    protected ConcurrentLinkedQueue<Integer> queue;

    public Consumer(ConcurrentLinkedQueue<Integer> queue) {
        this.queue = queue;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i=0; i<10; i++) {
            while (true) {
                //如果不peek则取到的都是null
                if (queue.peek() != null) {
                    System.out.println("消费了一个物品，" + queue.poll());
                    break;
                }
            }
//            System.out.println("消费了一个物品，" + queue.poll());
        }
    }
}
