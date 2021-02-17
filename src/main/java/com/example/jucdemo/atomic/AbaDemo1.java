package com.example.jucdemo.atomic;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

import lombok.SneakyThrows;

public class AbaDemo1 {

    @SneakyThrows
    public static void main(String[] args) {
        aba_desc();
        aba_fix();
    }

    @SneakyThrows
    private static void aba_fix() {
        AtomicStampedReference<Integer> atomicStampedRef =
                new AtomicStampedReference<Integer>(10, 0);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            atomicStampedRef.compareAndSet(10, 11,
                    atomicStampedRef.getStamp(), atomicStampedRef.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+" 当前版本：" + atomicStampedRef.getStamp()
                    + ",当前值：" + atomicStampedRef.getReference());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedRef.compareAndSet(11, 10,
                    atomicStampedRef.getStamp(), atomicStampedRef.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+" 当前版本：" + atomicStampedRef.getStamp()
                    + ",当前值：" + atomicStampedRef.getReference());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            int stamp = atomicStampedRef.getStamp();
            System.out.println("before sleep : stamp = " + stamp);
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("after sleep : stamp = " + atomicStampedRef.getStamp());
                boolean isSuccess = atomicStampedRef.compareAndSet(10, 12,
                        stamp, stamp+1);
                System.out.println("设置是否成功：" + isSuccess+ " 当前版本：" + atomicStampedRef.getStamp()
                        + ",当前值：" + atomicStampedRef.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println("aba_fix Done");
    }

    private static void aba_desc() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            atomicInteger.compareAndSet(10, 11);
            atomicInteger.compareAndSet(11, 10);
            System.out.println(Thread.currentThread().getName() + "：10->11->10");
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                boolean isSuccess = atomicInteger.compareAndSet(10, 12);
                System.out.println("设置是否成功：" + isSuccess + ",设置的新值：" + atomicInteger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println("aba_desc Done");
    }
}
