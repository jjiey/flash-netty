package the.flash.util;

import io.netty.channel.Channel;
import the.flash.attribute.Attributes;
import the.flash.session.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重构LoginUtil
 */
public class SessionUtil {

    /**
     * K->userId，V->channel
     */
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    /**
     * 登录的时候保存会话信息
     */
    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 登出的时候删除会话信息
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    /**
     * 判断是否登录
     */
    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }
}
