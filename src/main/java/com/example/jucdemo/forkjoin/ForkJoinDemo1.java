package com.example.jucdemo.forkjoin;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinDemo1 extends RecursiveTask<Long> {
    final long start; //开始计算的数
    final long end; //最后计算的数

    public ForkJoinDemo1(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        //如果计算量小于1000，那么分配一个线程执行if中的代码块，并返回执行结果
        if(end - start < 1000) {
//            System.out.println(Thread.currentThread().getName() + " 开始执行: " + start + "-" + end);
            long sum = 0;
            for(long i = start; i <= end; i++)
                sum += i;
            return sum;
        }
        //如果计算量大于1000，那么拆分为两个任务
        ForkJoinDemo1 task1 = new ForkJoinDemo1(start, (start + end) / 2);
        ForkJoinDemo1 task2 = new ForkJoinDemo1((start + end) / 2 + 1, end);
        //执行任务
        task1.fork();
        task2.fork();
        //获取任务执行的结果
        return task2.join() + task1.join();
    }

    private static void testForkJoin() {
        try {
            Instant start = Instant.now();
            ForkJoinPool pool = ForkJoinPool.commonPool();
            ForkJoinTask<Long> task = new ForkJoinDemo1(1,10000000l);
            pool.submit(task);

            Instant end = Instant.now();
            System.out.println("oldForkJoin-耗时：" + Duration.between(start, end).toMillis() + "ms");
            System.out.println("oldForkJoin-结果为：" + task.get()); // 打印结果500500
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void testExecutorService() {
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        ExecutorServiceCalculator cal = new ExecutorServiceCalculator();
        long result = cal.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("ExecutorService-耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("ExecutorService-结果为：" + result); // 打印结果500500
        cal.close();
    }

    public static void testForkJoinService(){
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        ForkJoinCalculator cal = new ForkJoinCalculator();
        long result = cal.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("ForkJoin-耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("ForkJoin-结果为：" + result); // 打印结果500500
    }

    public static void main(String[] args) {
//        testExecutorService();
        testForkJoinService();
//        testForkJoin();
//        StreamCalculator.sumUp();
    }
}
