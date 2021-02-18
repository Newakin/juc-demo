package com.example.jucdemo.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldDemo {
    public AtomicIntegerFieldUpdater<DataDemo> updater;

    public AtomicIntegerFieldDemo(String name){
        updater = AtomicIntegerFieldUpdater.newUpdater(DataDemo.class,name);
    }

    public static void main(String[] args) {
        AtomicIntegerFieldDemo af = new AtomicIntegerFieldDemo("publicVar");
        af.updatePublicVar();

        AtomicIntegerFieldDemo af2 = new AtomicIntegerFieldDemo("protectedVar");
        af2.updateProtectedVar();

//        AtomicIntegerFieldDemo af3 = new AtomicIntegerFieldDemo("privateVar");
//        af3.updatePrivateVar();

    }

    public void updatePublicVar(){
        DataDemo data = new DataDemo();
        System.out.println("publicVar = "+updater.addAndGet(data, 2));
    }

    public void updateProtectedVar(){
        DataDemo data = new DataDemo();
        System.out.println("protectedVar = "+updater.addAndGet(data,2));
    }

    public void updatePrivateVar(){
        DataDemo data = new DataDemo();
        System.out.println("privateVar = "+updater.addAndGet(data, 2));
    }

}


class DataDemo{
    public volatile int publicVar=3;
    protected volatile int protectedVar=4;
    private volatile  int privateVar=5;
    public volatile static int staticVar = 10;
    public volatile Integer integerVar = 19;
    public volatile Long longVar = 18L;
}
