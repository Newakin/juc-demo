package com.example.jucdemo.forkjoin;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

public class StreamCalculator {
    public static void sumUp(){
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0, 10000000l).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println("Stream-耗时：" + Duration.between(start, end).toMillis() + "ms");

        System.out.println("Stream-结果为：" + result); // 打印结果500500
    }
}
