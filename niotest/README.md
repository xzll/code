IO会阻塞，如果socket只在一个main线程中处理，那么其他socket对服务端的请求会被main线程的io所阻塞。
如果修改为多线程处理socket的io，那么其他socket连接服务端socket时，服务端会为这个socket创建一个线程来处理io，处理完后销毁。这样子有个问题，就是线程开启和销毁频繁，会影响到系统性能和资源。
且读写不能同时进行。

为了解决这个问题，使用线程池，有请求就用线程池里的空闲线程。
两个问题：频繁线程上下文切换会占用cpu，性能得不到有效利用。请求超过线程池线程数量。

## NIO

同步非阻塞

解决频繁线程切换：创建线程的时机拖到io的时候，要io再创建线程。Selector记录连接状态。ACCEPT、Readable、Writable。
解决非阻塞：缓冲区（数组），读写分离，切片，一部分是读的要写只能去后面写。

Selector.open()
根据操作系统获得selector

select/poll模型，转成了 epoll()模型。

---

《Java TCP IP Socket编程》

### Channel
信道的connect方法会立即返回，如果建立了连接返回true，否则返回false。
可以通过finishConnect()方法来轮询连接状态，这期间可以做其他的事。
 需要配置阻塞行为以实现非阻塞式的信道：```clntChan.configureBlocking(false); ```

###Selector
一个selector实力可以同时检查查（如果需要，也可以等待）一组信道的 I/O状态。
- 创建selector实例，使用静态工厂方法 open()
- 注册信道
- select方法阻塞等待，直到有一个或多个信道准备好了 I/O 操作或等待超时。

不使用多线程和忙等


### Buffer
Channel 使用的不是流，而是缓冲区来发送或读取数据。与流不同，缓冲区有固定的、有限的容量，并由内部（但可以被访问）状态记录了有多少数据放入或取出，就像是有限容量的队列一样。Buffer 是一个抽象类，只能通过创建它的子类来获得 Buffer 实例，而每个子类都设计为用来容纳一种 Java 基本数据类型（boolean 除外）。因此，这些实例分别为 FloatBuffer，或 IntBuffer，或 ByteBuffer，等等（ByteBuffer 是这些实例中最灵活的，并将在后面很多例子中用到）。在 channel 中使用 Buffer 实例通常不是使用构造函数创建的，而是通过调用allocate()方法创建指定容量的 Buffer 实例，```ByteBuffer buffer = ByteBuffer.allocate(CAPACITY); ```
或通过包装一个已有的数组来创建：```ByteBuffer buffer = ByteBuffer.wrap(byteArray); ```

- 在读写数据时，它有内部状态来跟踪缓冲区
的**当前位置**，以及**有效可读数据的结束位置**等。为了实现这些功能，每个缓冲区维护了指向其元素列表的 4 个索引，position、limit、capacity、mark。
- position，下一个要读写的元素
- limit，第一个不可读/写元素 
- capacity，数组大小

- flip()：固定limit在position的位置，等待信道的写操作（缓冲区的读操作get），信道的write会读取缓存区的position位置到limit位置的数据
```java
    /**
     * Flips this buffer.  The limit is set to the current position and then
     * the position is set to zero.  If the mark is defined then it is
     * discarded.
     *
     * <p> After a sequence of channel-read or <i>put</i> operations, invoke
     * this method to prepare for a sequence of channel-write or relative
     * <i>get</i> operations.  For example:
     *
     * <blockquote><pre>
     * buf.put(magic);    // Prepend header
     * in.read(buf);      // Read data into rest of buffer
     * buf.flip();        // Flip buffer
     * out.write(buf);    // Write header + data to channel</pre></blockquote>
     *
     * <p> This method is often used in conjunction with the {@link
     * java.nio.ByteBuffer#compact compact} method when transferring data from
     * one place to another.  </p>
     *
     * @return  This buffer
     */
    public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }
```
- clear() 准备信道的读操作read()（缓存区的put）
```java
    /**
     * Clears this buffer.  The position is set to zero, the limit is set to
     * the capacity, and the mark is discarded.
     *
     * <p> Invoke this method before using a sequence of channel-read or
     * <i>put</i> operations to fill this buffer.  For example:
     *
     * <blockquote><pre>
     * buf.clear();     // Prepare buffer for reading
     * in.read(buf);    // Read data</pre></blockquote>
     *
     * <p> This method does not actually erase the data in the buffer, but it
     * is named as if it did because it will most often be used in situations
     * in which that might as well be the case. </p>
     *
     * @return  This buffer
     */
    public final Buffer clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return this;
    }
```
- rewind()：准备重新从缓冲区读出相同的数据

```java

    /**
     * Rewinds this buffer.  The position is set to zero and the mark is
     * discarded.
     *
     * <p> Invoke this method before a sequence of channel-write or <i>get</i>
     * operations, assuming that the limit has already been set
     * appropriately.  For example:
     *
     * <blockquote><pre>
     * out.write(buf);    // Write remaining data
     * buf.rewind();      // Rewind buffer
     * buf.get(array);    // Copy data into array</pre></blockquote>
     *
     * @return  This buffer
     */
    public final Buffer rewind() {
        position = 0;
        mark = -1;
        return this;
    }
```
- compact()：压缩buffer中的数据，将 position 与 limit 之间的元素复制到缓冲区的开始位置，从而为后续的put()/read()调用让出空间。position 的值将设置为要复制的数据的长度，limit 的值将设置为capacity，mark 则变成未定义。
在调用 write()方法后和添加新数据的 read()方法前调用 compact()方法，则将所有"剩余"的数据移动到缓冲区的开头，从而为释放最大的空间来存放新数据。


通常，底层平台（操作系统）不能使用这些java缓冲区进行 I/O 操作。操作系统必须使用自己的缓冲区来进行 I/O，并将结果复制到java缓冲区的后援数组中。这些复制过程可能非常耗费系统资源，尤其是在有很多读写需求的时候。Java 的 NIO 提供了一种直接缓冲区（direct buffers）来解决这个问题。**使用直接缓冲区，Java 将从平台能够直接进行 I/O 操作的存储空间中为缓冲区分配后援存储空间，从而省略了数据的复制过程。**这种低层的、本地的 I/O 通常在字节层进行操作，因此**只能为 ByteBuffer 进行直接缓冲区分配**。

通过调用 ```isDirect()```方法可以查看一个缓冲区是否是直接缓冲区。由于直接缓冲区没有后援数组，在它上面调用 ```array()```或 ```arrayOffset()```方法都将抛出```UnsupportedOperationException```异常。
- **调用 allocateDirect()方法并不能保证能成功分配直接缓冲区**--有的平台或 JVM 可能不支持这个操作，因此在尝试分配直接缓冲区后必须调用isDirect()方法进行检查。
- **分配和销毁直接缓冲区通常比分配和销毁非直接缓冲区要消耗更多的系统资源**，因为直接缓冲区的后援存储空间通常存在与 JVM 之外，**对它的管理需要与操作系统进行交互**。所以，只有当需要在很多 I/O 操作上长时间使用时，才分配直接缓冲区。

使用io流读取操作系统的内容时，首先数据会加载到jvm内存中，然后从jvm内存读取数据。而DirectorBuffer 跨越jvm直接读取操作系统的数据，速度非常快。
IOUtil的read方法：
```java
    static int read(FileDescriptor var0, ByteBuffer var1, long var2, NativeDispatcher var4) throws IOException {
        if (var1.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        } else if (var1 instanceof DirectBuffer) {
            return readIntoNativeBuffer(var0, var1, var2, var4);
        } else {
            ByteBuffer var5 = Util.getTemporaryDirectBuffer(var1.remaining());

            int var7;
            try {
                int var6 = readIntoNativeBuffer(var0, var5, var2, var4);
                var5.flip();
                if (var6 > 0) {
                    var1.put(var5);
                }

                var7 = var6;
            } finally {
                Util.offerFirstTemporaryDirectBuffer(var5);
            }

            return var7;
        }
    }
```

- 使用了缓冲区复制操作，向网络写数据和写日志就可以在不同的线程中并行进行。
```java
ByteBuffer logBuffer = buffer.duplicate();
while (buffer.hasRemaining()) // Write all data to network
networkChannel.write(buffer);
while (logBuffer.hasRemaining()) // Write all data to 
logger
loggerChannel.write(buffer); 
```

- slice()方法用于创建一个共享了原始缓冲区子序列的新缓冲区。新缓冲区的 position 值是 0，而其 limit 和capacity 的值都等于原始缓冲区的 limit 和position 的差值。slice()方法将新缓冲区数组的 offset 值设置为原始缓冲区的position 值，然而，在新缓冲区上调用 array()方法还是会返回整个数组。
