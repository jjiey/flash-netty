package the.flash;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 服务端启动流程
 */
public class NettyServer {

    private static final int BEGIN_PORT = 8000;

    public static void main(String[] args) {
        // boosGroup：监听端口，accept新连接的线程组
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        // workerGroup：处理每一条连接的数据读写的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 引导类ServerBootstrap，引导我们进行服务端的启动工作
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap
                // 必须1.指定线程模型，给引导类配置两大线程组
                .group(boosGroup, workerGroup)
                // 必须2.指定IO模型，这里指定IO模型为NIO，也可指定别的比如BIO（OioServerSocketChannel）
                .channel(NioServerSocketChannel.class)
                // 给服务端的channel（NioServerSocketChannel）指定一些自定义属性，后续可以通过channel.attr()取出该属性，说白了就是给服务端的channel维护一个map而已
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                // 给每条新连接指定自定义属性，后续可以通过channel.attr()取出该属性
                .childAttr(clientKey, "clientValue")
                // option()给服务端channel设置一些属性
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // childOption()给每条新连接设置一些TCP底层相关的属性
                // 是否开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 是否开启Nagle算法，true表示关闭，false表示开启。通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 必须3.指定后续每条新连接数据的读写业务处理逻辑
                /* NioServerSocketChannel和NioSocketChannel两个概念 可以对应 BIO编程模型中的ServerSocket和Socket两个概念 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 取出之前给每一条新连接指定自定义属性
                        System.out.println(ch.attr(clientKey).get());
                    }
                })
                // 指定在服务端启动过程中的一些逻辑
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务端启动中");
                    }
                });

        // 必须4.监听端口
        bind(serverBootstrap, BEGIN_PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        // bind()方法是一个异步方法，调用之后立即返回ChannelFuture。给ChannelFuture添加监听器GenericFutureListener，重写operationComplete方法，监听端口是否绑定成功
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
