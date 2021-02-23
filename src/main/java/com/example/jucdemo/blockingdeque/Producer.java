package com.example.jucdemo.blockingdeque;

import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class Producer implements Runnable{
    protected BlockingDeque deque;

    public Producer(BlockingDeque deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        try {
            for (int i=0; i<3; i++) {
                deque.putFirst(i);
                deque.putLast(i);
                System.out.println("生产了两个个物品,"+i);
                Thread.sleep(new Random().nextInt(5000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
