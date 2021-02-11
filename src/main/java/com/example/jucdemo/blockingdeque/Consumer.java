package com.example.jucdemo.blockingdeque;

import java.util.concurrent.BlockingDeque;

public class Consumer implements Runnable{
    protected BlockingDeque deque;

    public Consumer(BlockingDeque deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        try {
            for (int i=0; i<3; i++) {
                System.out.println("消费了一个物品，"+deque.takeFirst());
                System.out.println("消费了一个物品，"+deque.takeLast());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
