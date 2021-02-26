package com.example.jucdemo.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 斐波那契数列: 1、1、2、3、5、8、13、21、34、…… 公式 : F(1)=1，F(2)=1, F(n)=F(n-1)+F(n-2)(n>=3，n∈N*)
 */
public class Fibonacci extends RecursiveTask<Integer> {
    final int n;
    public Fibonacci(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        /**
         * 可以两个任务都fork，要注意的是两个任务都fork的情况，必须按照f1.fork()，f2.fork()， f2.join()，f1.join()这样的顺序，不然有性能问题
         */
        Fibonacci f1 = new Fibonacci(n - 1);
        Fibonacci f2 = new Fibonacci(n - 2);

        /**
         * 方法一，compute() 会使用当前线程来执行。
         * 6765, 8ms
         */
//        f1.fork();
//        return f2.compute() + f1.join();

        /**
         * 方法二， invokeAll会把传入的任务的第一个交给当前线程来执行，其他的任务都fork加入工作队列，这样等于利用当前线程也执行任务了
         * 6765, 8ms
         */
//        invokeAll(f1,f2);
//        return f2.join() + f1.join();

        /**
         * 方法三， fork()/join()
         * 6765, 9ms
         */
        f1.fork();
        f2.fork();
        return f2.join()+f1.join();

    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Fibonacci fibonacci = new Fibonacci(20);
        long startTime = System.currentTimeMillis();
        Integer result = forkJoinPool.invoke(fibonacci);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }
}
