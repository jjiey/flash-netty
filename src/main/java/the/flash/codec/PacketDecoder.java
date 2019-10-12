package the.flash.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import the.flash.protocol.PacketCodeC;

import java.util.List;

/**
 * 自定义解码，二进制数据转换到对象
 * 4.1.6.Final版本ByteBuf默认情况下用的是堆外内存，ByteToMessageDecoder会自动进行内存的释放
 * 基于ByteToMessageDecoder，我们可以实现自定义解码，而不用关心ByteBuf的强转和解码结果的传递
 */
public class PacketDecoder extends ByteToMessageDecoder {

    /**
     * @param in ByteBuf类型
     * @param out 添加解码后的结果对象，就可以自动实现结果往下一个handler进行传递
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
