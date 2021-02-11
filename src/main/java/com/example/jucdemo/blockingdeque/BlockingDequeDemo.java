package com.example.jucdemo.blockingdeque;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockingDequeDemo {
    @SneakyThrows
    public static void main(String[] args) {
        BlockingDeque blockingDeque = new LinkedBlockingDeque(10);

        Producer producer = new Producer(blockingDeque);
        Consumer consumer = new Consumer(blockingDeque);
        new Thread(producer).start();
        new Thread(consumer).start();
    }

}
