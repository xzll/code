# Lock
lock中定义了4中方法来获取锁。  
lock必须手动释放锁，所以需要在trycatch中使用并在finally中释放锁避免死锁。  

void lock(); 阻塞等待锁  
void lockInterruptibly() throws InterruptedException;   
boolean tryLock();  尝试获取锁，会立即返回
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;  有等待时间的获取锁
void unlock();  释放锁
Condition newCondition();  在等待锁的过程中可响应中断

## ReentrantLock
# ReadWriteLock

Lock readLock();  
Lock writeLock();  

## ReentrantReadWriteLock  

# 
可重入  
可中断  
公平锁  
读写锁  
https://www.cnblogs.com/baizhanshi/p/6419268.html