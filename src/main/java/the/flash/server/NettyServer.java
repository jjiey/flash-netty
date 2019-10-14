package the.flash.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import the.flash.codec.PacketDecoder;
import the.flash.codec.PacketEncoder;
import the.flash.codec.Spliter;
import the.flash.server.handler.LifeCyCleTestHandler;
import the.flash.server.handler.LoginRequestHandler;
import the.flash.server.handler.MessageRequestHandler;

import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                /*
                ChannelInitializer就利用了Netty的handler生命周期中handlerAdded()与channelRegistered()两个特性
                handlerAdded()：当检测到新连接之后，调用ch.pipeline().addLast(new LifeCyCleTestHandler())之后的回调，表示在当前的channel中，已经成功添加了一个handler处理器
                channelRegistered()：当前的channel的所有的逻辑处理已经和某个NIO线程建立了绑定关系
                 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    /**
                     * ChannelInitializer定义了一个抽象的方法initChannel()，这个抽象方法由我们自行实现，我们在服务端启动的流程里面的实现逻辑就是往pipeline里面塞我们的handler链
                     */
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new LifeCyCleTestHandler());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
