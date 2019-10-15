package the.flash.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import the.flash.protocol.Packet;
import the.flash.protocol.PacketCodec;

import java.util.List;

/**
 * 编解码操作
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {

    }

    /**
     * 将二进制数据ByteBuf转换为java对象Packet
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        out.add(PacketCodec.INSTANCE.decode(byteBuf));
    }

    /**
     * 将java对象Packet转换为二进制数据ByteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
        // 调用channel的内存分配器手工分配ByteBuf
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        out.add(byteBuf);
    }
}
