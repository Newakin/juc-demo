package com.example.jucdemo.threadpoolexecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MonitorThread implements Runnable{
    private ThreadPoolExecutor executor;

    private int seconds;

    private boolean run=true;

    public MonitorThread(ThreadPoolExecutor executor, int delay)
    {
        this.executor = executor;
        this.seconds=delay;
    }

    public void shutdown(){
        this.run=false;
    }
    @Override
    public void run() {
        while(run){
            System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));
            try {
                Thread.sleep(seconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE  = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                // 核心线程数为 ：5
                CORE_POOL_SIZE,
                // 最大线程数 ：10
                MAX_POOL_SIZE,
                // 等待时间 ：1L
                KEEP_ALIVE_TIME,
                // 等待时间的单位 ：秒
                TimeUnit.SECONDS,
                // 任务队列为 ArrayBlockingQueue，且容量为 100
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                // 饱和策略为 CallerRunsPolicy
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        MonitorThread monitorThread = new MonitorThread(executor,1);
        executor.execute(monitorThread);
    }
}
