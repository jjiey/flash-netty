package the.flash.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import the.flash.attribute.Attributes;

public class LoginUtil {

    /**
     * 绑定登录成功的标志位
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 判断是否登录成功
     */
    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }
}
