package nettybeginner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 编码器，将UnixTime转换为ByteBuf
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);//申请内存
        encoded.writeInt((int) m.value());//返回32位整数
        ctx.write(encoded, promise); // (1)传入promise是为了能够在编码数据写入时，将其标记为成功或失败？？
    }
}
