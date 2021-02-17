## Atomic
java原子类底层是CAS实现的，而CAS底层是Unsafe类实现的。  
CAS+原子操作实现非阻塞同步。  

### CAS
Compare-And-Swap，让CPU先进行两个值的比较，如果不同则原子地更新某个位置的值。  
CAS是基于硬件的汇编指令实现的，靠的是硬件，JVM封装了这些汇编调用，Atomicxxx也是使用了这些接口。  

### CAS的使用
在不适用原子操作的时候我们会加锁。  
```java
public class Test {
    private int i=0;
    public synchronized int add(){
        return i++;
    }
}
```

使用AtomicInteger原子操作。  
```java
public class Test {
    private  AtomicInteger i = new AtomicInteger(0);
    public int add(){
        return i.addAndGet(1);
    }
}
```

### ABA问题
线程1和线程2并发访问ConcurrentStack  
线程1执行出栈【预期结果是弹出A，B成为栈顶】，但在读取栈顶A之后，被线程2抢占  
![ABA-1.png](../pics/ABA-1.png)   
线程2记录栈顶A，依次弹出A和B，再依次将D，C，A入栈，此时B处于游离状态。  
![ABA-2.png](../pics/ABA-2.png)  
此时轮到线程T1执行CAS操作，检测发现栈顶仍为A，所以CAS成功，栈顶变为B，但实际上B.next为null，C D丢了  
![ABA-3.png](../pics/ABA-3.png)  

ABA问题实例 [**AbaDemo1.java**](../main/java/com/example/jucdemo/atomic/AbaDemo1.java)

### UnSafe类


### 示例
* [**AbaDemo1.java**](../main/java/com/example/jucdemo/atomic/AbaDemo1.java)




