package nettybeginner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder { // (1)是ChannelInboundHandler的一个实现，可以很容易的解决分片问题
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)接收到新数据时，会调用decode方法
        if (in.readableBytes() < 4) {
            return; // (3)接收到更多数据时，会再次调用decode
        }

//        out.add(in.readBytes(4)); // (4)将一个对象添加到out中，表示解码器成功解码了消息。丢弃缓冲区读取的部分
        out.add(new UnixTime(in.readUnsignedInt()));//向out添加UnixTime对象
    }

}
