package nettybeginner;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TIMEServer {
    private int port;

    public TIMEServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        // (1)EventLoopGroup 处理io操作的多线程事件循环？
        //使用多少线程以及他们如何映射到创建的通道取决于它的实现
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //接收传入的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理连接
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)设置服务器
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)指定使用NioServerSocketChannel类来实例化一个新的Channel来接受传入的连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)配置channel
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new TIMEServerHandler());
                            //添加编码器，服务器将UnixTime对象编码为ByteBuf，然后传给客户端
                            //客户端收到这个ByteBuf后通过解码器将它解码为UnixTime对象，打印
                            ch.pipeline().addLast(new TIMEServerHandler(),new TimeEncoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)设置父？channel参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)设置子？channel参数

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)绑定所有NIC（网络接口卡）？的端口到8080
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        new TIMEServer(port).run();
    }
}
