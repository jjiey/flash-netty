package the.flash.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author chao.yu
 * chao.yu@dianping.com
 * @date 2018/08/04 06:23.
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接成功后会回调逻辑处理器的channelActive()方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端写出数据");
        // 1.获取数据
        ByteBuf buffer = getByteBuf(ctx);
        // 2.写数据
        ctx.channel().writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1.获取netty对二进制数据的抽象ByteBuf
        // ctx.alloc()获取到一个ByteBuf的内存管理器，这个内存管理器的作用就是分配一个ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();
        // 2.准备数据，指定字符串的字符集为utf-8
        byte[] bytes = "你好，闪电侠!".getBytes(Charset.forName("utf-8"));
        // 3.填充字符串的二进制数据到ByteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }

    /**
     * 收到数据之后都会调用到channelRead方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }
}
