package nettybeginner;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by moon on 2017/4/6.
 */
public class TimeClient {
    public static void main(String[] args) {
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
        String host = "127.0.0.1";
        int port = Integer.parseInt("8080");
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)用于非服务器通道
            b.group(workerGroup); // (2)该group同时用作一个老板组和一个工作人员组
            b.channel(NioSocketChannel.class); // (3)指定NioSocketChannel来实例化客户端通道
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {// (4)不使用childHandler 因为客户端SocketChannel没有父级
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());
                    ch.pipeline().addLast(new TimeDecoder(),new TimeClientHandler());
                }

            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)用connect 不用bind

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }


    }
}
