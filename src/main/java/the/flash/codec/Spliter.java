package the.flash.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import the.flash.protocol.PacketCodeC;

public class Spliter extends LengthFieldBasedFrameDecoder {

    /**
     * 长度域相对整个数据包的偏移量4+1+1+1=7
     */
    private static final int LENGTH_FIELD_OFFSET = 7;
    /**
     * 长度域的长度
     */
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        /**
         * 数据包的最大长度，长度域相对整个数据包的偏移量，长度域的长度
         */
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    /**
     * @param in 每次传递进来的时候，均为一个数据包的开头
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的客户端，关闭连接，节省资源
        if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
