package the.flash;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 客户端启动流程
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 启动引导类Bootstrap，负责启动客户端以及连接服务端
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 必须1.指定线程模型
                .group(workerGroup)
                // 必须2.指定IO模型为NIO，也可指定别的比如BIO（OioSocketChannel）
                .channel(NioSocketChannel.class)
                // 给客户端Channel（NioSocketChannel）绑定自定义属性，后续可以通过channel.attr()取出该属性，说白了就是给客户端的channel维护一个map而已
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                // 设置TCP底层相关属性
                // 连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 是否开启 TCP 底层心跳机制，true 为开启
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 是否开启Nagle算法，true表示关闭，false表示开启。通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启
                .option(ChannelOption.TCP_NODELAY, true)
                // 必须3.IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                    }
                });

        // 必须4.建立连接
        connect(bootstrap, "juejin.im", 80, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        // connect()方法是一个异步方法，调用之后立即返回ChannelFuture。给ChannelFuture添加监听器GenericFutureListener，重写operationComplete方法，监听连接是否成功
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                // bootstrap.config()方法返回的是BootstrapConfig，他是对Bootstrap配置参数的抽象
                // bootstrap.config().group()返回的是一开始配置的线程模型workerGroup
                // 调用workerGroup的schedule方法实现定时任务逻辑
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}
