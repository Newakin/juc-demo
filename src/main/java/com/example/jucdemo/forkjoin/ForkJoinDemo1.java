package com.example.jucdemo.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo1 extends RecursiveTask<Integer> {
    final int start; //开始计算的数
    final int end; //最后计算的数

    public ForkJoinDemo1(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        //如果计算量小于1000，那么分配一个线程执行if中的代码块，并返回执行结果
        if(end - start < 1000) {
            System.out.println(Thread.currentThread().getName() + " 开始执行: " + start + "-" + end);
            int sum = 0;
            for(int i = start; i <= end; i++)
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
        return task1.join() + task2.join();
    }

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
//        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> task = new ForkJoinDemo1(1,10000);
        pool.submit(task);
        try {
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
