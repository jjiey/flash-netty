package the.flash.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 服务端空闲检测
 */
public class IMIdleStateHandler extends IdleStateHandler {

    /**
     * 如果15秒内没有读到数据，就表示连接假死
     */
    private static final int READER_IDLE_TIME = 15;

    public IMIdleStateHandler() {
        // 读空闲时间，指的是在这段时间内如果没有数据读到，就表示连接假死；
        // 写空闲时间，指的是在这段时间如果没有写数据，就表示连接假死；
        // 读写空闲时间，表示在这段时间内如果没有产生数据读或者写，就表示连接假死。
        // 写空闲和读写空闲为0，表示我们不关心者两类条件
        // 时间单位
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    /**
     * 连接假死之后会回调channelIdle()方法
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}
