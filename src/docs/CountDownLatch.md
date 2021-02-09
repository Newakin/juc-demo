## CountDownLatch

1.底层是内部类Sync继承了AQS(Abstract Queued Synchronizer)  
2.主要用法是将一个任务分为n个互相独立的支线任务，主线程等待，直到所有支线任务都执行完才继续执行主线程。
### 内部类Sync

```java
private static final class Sync extends AbstractQueuedSynchronizer {
    // 版本号
    private static final long serialVersionUID = 4982264981922014374L;

    // 构造器
    Sync(int count) {
        setState(count);
    }

    // 返回当前计数
    int getCount() {
        return getState();
    }

    // 试图在共享模式下获取对象状态
    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;
    }

    // 试图设置状态来反映共享模式下的一个释放
    protected boolean tryReleaseShared(int releases) {
        // Decrement count; signal when transition to zero
        // 无限循环
        for (;;) {
            // 获取状态
            int c = getState();
            if (c == 0) // 没有被线程占有
                return false;
            // 下一个状态
            int nextc = c-1;
            if (compareAndSetState(c, nextc)) // 比较并且设置成功
                return nextc == 0;
        }
    }
}
```
### 核心函数
* await()  
  此函数会再当前线程的倒计数归零之前一直等待，除非线程被中断。  
* countDown()  
  此函数会递减计数器，如果归零则释放所有之前等待的线程。  

### 示例
* [**CountDownLatchConsumer.java**](../main/java/com/example/jucdemo/countdownlatch/CountDownLatchConsumer.java)





