package the.flash.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.request.HeartBeatRequestPacket;

import java.util.concurrent.TimeUnit;

/**
 * 客户端定时发心跳
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每隔5秒，向服务端发送一个心跳数据包，这个时间段通常要比服务端的空闲检测时间的一半要短一些，这里直接定义为空闲检测时间的三分之一，主要是为了排除公网偶发的秒级抖动
     * 实际在生产环境中，我们的发送心跳间隔时间和空闲检测时间可以略长一些，可以设置为几分钟级别，具体应用可以具体对待，没有强制的规定
     */
    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);

        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        // ctx.executor()返回的是当前的channel绑定的NIO线程
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
