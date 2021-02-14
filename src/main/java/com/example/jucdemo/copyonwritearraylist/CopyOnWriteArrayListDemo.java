package com.example.jucdemo.copyonwritearraylist;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CopyOnWriteArrayListDemo {
    private static CopyOnWriteArrayList<Integer> cowal = new CopyOnWriteArrayList<>();

    public void handleMsg() {
        for (int i = 100; i<110; i++) {
            cowal.add(i);
        }
    }

    public static void main(String[] args) {

        CopyOnWriteArrayListDemo consumer = new CopyOnWriteArrayListDemo();
        for (int i = 0; i < 10; i++) {
            cowal.add(i);
        }
        new Thread(() -> {
            consumer.handleMsg();
        }).start();

        Iterator<Integer> iterator = cowal.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        iterator = cowal.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }

}
