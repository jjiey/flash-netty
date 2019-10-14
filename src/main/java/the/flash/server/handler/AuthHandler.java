package the.flash.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.util.LoginUtil;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            // 如果未登录，直接强制关闭连接
            ctx.channel().close();
        } else {
            // 如果已经经过权限认证，那么就直接调用pipeline的remove()方法删除自身，这里的this指的其实就是AuthHandler这个对象，删除之后，这条客户端连接的逻辑链中就不再有这段逻辑了
            // 主要是为了解决这种情况：如果客户端已经登录成功了，那么在每次处理客户端数据之前，我们都要经历这么一段逻辑，比如，平均每次用户登录之后发送100次消息，其实剩余的99次身份校验逻辑都是没有必要的，因为只要连接未断开，客户端只要成功登录过，后续就不需要再进行客户端的身份校验。这里身份认证逻辑比较简单，实际生产环境中，身份认证的逻辑可能会更加复杂，我们需要寻找一种途径来避免资源与性能的浪费，使用pipeline的热插拔机制可以实现这一点
            ctx.pipeline().remove(this);
            // 如果登录，就把读到的数据向下传递，传递给后续指令处理器
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }
}
