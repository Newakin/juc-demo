package com.example.jucdemo.exchanger;

import java.util.concurrent.Exchanger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangerConsumer {
    @SneakyThrows
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                log.info("{}:我有白粉，准备交换钱……",Thread.currentThread().getName());
                Thread.sleep(5000);
                /*
                 *  在此处等待另外一个线程到来，并进行数据交换，如果没有另一个线程到来，那么当前这个线程会处于休眠状态，直到3件事情发生：
                 *  1、等待另一个线程到达交换点
                 *  2、被另一个线程中断(警察赶来了，打断了交易)
                 *  3、等待超时，当调用exchanger.exchange(x, timeout, unit)方法时有效(毒贩查觉到危险，没有来交易)
                 */
                String result = exchanger.exchange("白粉");
                log.info("{}:用白粉换回来的为:{}",Thread.currentThread().getName(),result);
            } catch (InterruptedException e) {
                log.error("error,{}",e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try {
                log.info("{}:我有钱，准备交换白粉……",Thread.currentThread().getName());
                Thread.sleep(2000);
                String result = exchanger.exchange("钱");
                log.info("{}:用钱换回来的为:{}",Thread.currentThread().getName(),result);
            } catch (InterruptedException e) {
                log.error("error,{}",e.getMessage());
            }
        }).start();


    }

}
