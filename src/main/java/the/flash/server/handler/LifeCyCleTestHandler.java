package the.flash.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelHandler回调方法的执行顺序：
 * handlerAdded() -> channelRegistered() -> channelActive() -> channelRead() -> channelReadComplete()
 * 把客户端关闭，ChannelHandler回调方法的执行顺序为：
 * channelInactive() -> channelUnregistered() -> handlerRemoved()
 */
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当检测到新连接之后，调用ch.pipeline().addLast(new LifeCyCleTestHandler())之后的回调，表示在当前的channel中，已经成功添加了一个handler处理器
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被添加：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * 当前的channel的所有的逻辑处理已经和某个NIO线程建立了绑定关系
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 绑定到线程(NioEventLoop)：channelRegistered()");
        super.channelRegistered(ctx);
    }

    /**
     * 当channel的所有的业务逻辑链准备完毕以及绑定好一个NIO线程之后，这条连接算是真正激活了，接下来就会回调到此方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 准备就绪：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 客户端向服务端发来数据，每次都会回调此方法，表示有数据可读
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 服务端每次读完一次完整的数据之后，回调该方法，表示数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 某次数据读完：channelReadComplete()");
        super.channelReadComplete(ctx);

    }

    /**
     * 表示这条连接已经被关闭了，这条连接在TCP层面已经不再是ESTABLISH状态了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 被关闭：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     * 既然连接已经被关闭，那么与这条连接绑定的线程就不需要对这条连接负责了，这个回调就表明与这条连接对应的NIO线程移除掉对这条连接的处理
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    /**
     * 移除掉所有给这条连接上添加的业务逻辑处理器
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
