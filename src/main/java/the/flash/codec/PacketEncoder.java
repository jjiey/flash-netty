package the.flash.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import the.flash.protocol.Packet;
import the.flash.protocol.PacketCodeC;

/**
 * 自定义编码，对象转换到二进制数据
 * 基于MessageToByteEncoder，可以实现自定义编码，而不用关心ByteBuf的创建，不用每次向对端写Java对象都进行一次编码
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    /**
     * 这个方法里面要做的事情就是把Java对象里面的字段写到ByteBuf，我们不再需要自行去分配ByteBuf
     * @param ctx
     * @param packet Java对象
     * @param out ByteBuf对象
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
