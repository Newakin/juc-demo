package com.example.jucdemo.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayDemo {
    public static void main(String[] args) {
        AtomicIntegerArray atarray = new AtomicIntegerArray(new int[]{1,2});
        System.out.println(atarray);
        System.out.println(atarray.getAndAdd(1,2));
        System.out.println(atarray.get(1));
    }
}
