package com.example.jucdemo.atomic;

import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class AtomicReferenceDemo {

    public static void main(String[] args) {
        Car c1 = Car.builder().id(1).build();
        Car c2 = Car.builder().id(2).build();
        // 新建AtomicReference对象，初始化它的值为c1对象
        AtomicReference ar = new AtomicReference(c1);
        // 通过CAS设置ar。如果ar的值为c1的话，则将其设置为c2。
        ar.compareAndSet(c1, c2);

        Car c3 = (Car) ar.get();
        System.out.println("c3 is " + c3);
        System.out.println("c3.equals(c1)=" + c3.equals(c1));
        System.out.println("c3.equals(c2)=" + c3.equals(c2));
    }
}

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
class Car {
    volatile long id;
}
