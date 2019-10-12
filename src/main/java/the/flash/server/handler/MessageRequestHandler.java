package the.flash.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.request.MessageRequestPacket;
import the.flash.protocol.response.MessageResponsePacket;

import java.util.Date;

/**
 * SimpleChannelInboundHandler把类型判断和对象传递的活都自动帮我们实现了，而我们只需要专注于处理所关心的指令即可
 * 基于SimpleChannelInboundHandler，我们可以实现每一种指令的处理，不再需要强转，不再有冗长乏味的if else逻辑，不需要手动传递对象
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
        messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");

        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
