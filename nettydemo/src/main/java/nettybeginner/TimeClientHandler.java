package nettybeginner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//        ByteBuf m = (ByteBuf) msg; // (1)
//        try {
//            long currentTimeMills = (m.readUnsignedInt() - 2208988800L) * 1000L;//从服务端接收一个32位整数，将其翻译成可读的形式
//            System.out.println(new Date(currentTimeMills));
//            ctx.close();
//        } finally {
//            m.release();
//        }

        UnixTime m = (UnixTime) msg;//接受UnixTime对象
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}