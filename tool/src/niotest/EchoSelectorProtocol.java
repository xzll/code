package niotest;

import java.nio.channels.SelectionKey;

import java.nio.channels.SocketChannel;

import java.nio.channels.ServerSocketChannel;

import java.nio.ByteBuffer;

import java.io.IOException;



public class EchoSelectorProtocol implements TCPProtocol {

    private int bufSize; // I/O buffer 的大小

    public EchoSelectorProtocol(int bufSize) {
        this.bufSize = bufSize;
    }

    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();//从键中获取信道，并接受连接
        clntChan.configureBlocking(false); // 必须是非阻塞模式，否则无法注册选择器
        // Register the selector with new channel for read and attach byte buffer
        clntChan.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
        //通过selectKey类的selector方法来获取相应的selector，这里的ByteBuffer实例参数会与其关联
    }



    public void handleRead(SelectionKey key) throws IOException {

        // Client socket channel has pending data

        SocketChannel clntChan = (SocketChannel)  key.channel();//获取信道
        ByteBuffer buf = (ByteBuffer) key.attachment();//获取关联的缓冲区
        long bytesRead = clntChan.read(buf);//读数据，如果返回的是-1 则表示底层socket连接已关闭
        if (bytesRead == -1) { // Did the other end close?
            clntChan.close();//关闭信道
        } else if (bytesRead > 0) {
            // Indicate via key that reading/writing are both of interest now.
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);//接收完数据将其标记为可写？？

        }
    }



    public void handleWrite(SelectionKey key) throws IOException {
        /*
         * Channel is available for writing, and key is valid (i.e., client channel not closed).
         */
        // Retrieve data read earlier
        ByteBuffer buf = (ByteBuffer) key.attachment();//获取缓存区，它包含了之前从信道中读取的数据
        buf.flip(); // Prepare buffer for writing 准备缓冲区的写操作，使写操作开始消耗有读操作产生的数据
        SocketChannel clntChan = (SocketChannel)  key.channel();
        clntChan.write(buf);
        if (!buf.hasRemaining()) { // Buffer completely  written?
            // Nothing left, so no longer interested in writes
            key.interestOps(SelectionKey.OP_READ);
        }
        buf.compact(); // Make room for more data to be readmin 压缩缓冲区
    }

}
