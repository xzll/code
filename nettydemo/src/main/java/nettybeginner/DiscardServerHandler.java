package nettybeginner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    // (1)ChannelInboundHandlerAdapter 提供可以覆盖的各种事件处理程序方法
    // (2)重写channelRead()方法，每当从客户端收到数据时，都会使用接收到的消息调用此方法？？
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // (3)为了实现discard协议，处理程序必须忽略收到的消息。
//        ((ByteBuf) msg).release(); //释放byteBuf
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            in.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (5)有异常时将会调用exceptionCaught方法
        cause.printStackTrace();
        ctx.close();
    }
}
