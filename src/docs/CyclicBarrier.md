## CyclicBarrier

1.底层是基于ReentranLock和AQS(Abstract Queued Synchronizer)实现的。  
2.理解为CyclicBarrier是一场运动会-三项全能，里面有游泳、公路自行车、长跑等，运动员们为多个线程，
当所有运动员都完成了游泳后才能开启公路自行车，都完成了公路自行车后才能开启长跑。全部三项比赛都
完成了则整个运动会自动结束。  
3.不会阻塞主线程
### 类构造函数

```java
public CyclicBarrier(int parties, Runnable barrierAction) {
    // 参与的线程数量小于等于0，抛出异常
    if (parties <= 0) throw new IllegalArgumentException();
    // 设置parties
    this.parties = parties;
    // 设置count
    this.count = parties;
    // 设置barrierCommand
    this.barrierCommand = barrierAction;
}
```
parties：线程数量。  
barrierAction:最后一个进入屏障的线程执行收尾。  
```java
public CyclicBarrier(int parties) {
    // 调用含有两个参数的构造函数
    this(parties, null);
}
```
只设置线程数，无后续动作。  
### 核心函数
* dowait()  
await()的底层实现，子线程等待。
```java
private int dowait(boolean timed, long nanos)
    throws InterruptedException, BrokenBarrierException,
            TimeoutException {
    // 保存当前锁
    final ReentrantLock lock = this.lock;
    // 锁定
    lock.lock();
    try {
        // 保存当前代
        final Generation g = generation;
        
        if (g.broken) // 屏障被破坏，抛出异常
            throw new BrokenBarrierException();

        if (Thread.interrupted()) { // 线程被中断
            // 损坏当前屏障，并且唤醒所有的线程，只有拥有锁的时候才会调用
            breakBarrier();
            // 抛出异常
            throw new InterruptedException();
        }
        
        // 减少正在等待进入屏障的线程数量
        int index = --count;
        if (index == 0) {  // 正在等待进入屏障的线程数量为0，所有线程都已经进入
            // 运行的动作标识
            boolean ranAction = false;
            try {
                // 保存运行动作
                final Runnable command = barrierCommand;
                if (command != null) // 动作不为空
                    // 运行
                    command.run();
                // 设置ranAction状态
                ranAction = true;
                // 进入下一代
                nextGeneration();
                return 0;
            } finally {
                if (!ranAction) // 没有运行的动作
                    // 损坏当前屏障
                    breakBarrier();
            }
        }

        // loop until tripped, broken, interrupted, or timed out
        // 无限循环
        for (;;) {
            try {
                if (!timed) // 没有设置等待时间
                    // 等待
                    trip.await(); 
                else if (nanos > 0L) // 设置了等待时间，并且等待时间大于0
                    // 等待指定时长
                    nanos = trip.awaitNanos(nanos);
            } catch (InterruptedException ie) { 
                if (g == generation && ! g.broken) { // 等于当前代并且屏障没有被损坏
                    // 损坏当前屏障
                    breakBarrier();
                    // 抛出异常
                    throw ie;
                } else { // 不等于当前带后者是屏障被损坏
                    // We're about to finish waiting even if we had not
                    // been interrupted, so this interrupt is deemed to
                    // "belong" to subsequent execution.
                    // 中断当前线程
                    Thread.currentThread().interrupt();
                }
            }

            if (g.broken) // 屏障被损坏，抛出异常
                throw new BrokenBarrierException();

            if (g != generation) // 不等于当前代
                // 返回索引
                return index;

            if (timed && nanos <= 0L) { // 设置了等待时间，并且等待时间小于0
                // 损坏屏障
                breakBarrier();
                // 抛出异常
                throw new TimeoutException();
            }
        }
    } finally {
        // 释放锁
        lock.unlock();
    }
}
```  

* nextGeneration()  
  一项运动结束了，所有运动员进入下一项运动。
```java
private void nextGeneration() {
    // signal completion of last generation
    // 唤醒所有线程 AQS方法
    trip.signalAll();
    // set up next generation
    // 恢复正在等待进入屏障的线程数量
    count = parties;
    // 新生一代
    generation = new Generation();
}
```  
* breakBarrier()
打破当前屏障，唤醒所有屏障中的线程，
```java
private void breakBarrier() {
    // 设置状态
    generation.broken = true;
    // 恢复正在等待进入屏障的线程数量
    count = parties;
    // 唤醒所有线程
    trip.signalAll();
}
```


### 示例
* [**CyclicBarrierConsumer.java**](../main/java/com/example/jucdemo/cyclicbarrier/CyclicBarrierConsumer.java)

### 和CountDownLatch对比
* CountDownLatch减计数，CyclicBarrier加计数。 
* CountDownLatch是一次性的，CyclicBarrier可以重用。 
* CountDownLatch和CyclicBarrier都有让多个线程等待同步然后再开始下一步动作的意思，
  但是CountDownLatch的下一步的动作实施者是主线程，具有不可重复性；
  而CyclicBarrier的下一步动作实施者还是“其他线程”本身，具有往复多次实施动作的特点。



