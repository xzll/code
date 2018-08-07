package nettybeginner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class TIMEServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 在建立连接时立即调用？
     * @param  ctx
     * @throws Exception
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        final ByteBuf time = ctx.alloc().buffer(4);//32位整数至少需要4个字节
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));//32位整数
//        ChannelHandlerContext.write（）（和writeAndFlush（））方法返回一个ChannelFuture
        final ChannelFuture f = ctx.writeAndFlush(time);//ChannelFuture表示尚未发生的I/O操作
        //连接建立后立即发送时间，发送完后关闭连接
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                assert f == future;
                ctx.close();
            }
        });
        //即使在发送消息之前，以下代码也可能会关闭连接：
        //Channel ch = ...;
        //ch.writeAndFlush(message);
        //ch.close();
        //因为所有操作在Netty中都是异步的。
        //所以需要在ChannelFuture完成之后调用close（）方法
//        注意，ch.close（）也可能不会立即关闭连接，并且它返回ChannelFuture。

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
