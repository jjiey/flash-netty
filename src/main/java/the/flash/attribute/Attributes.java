package the.flash.attribute;

import io.netty.util.AttributeKey;

/**
 * 客户端连接Channel属性
 */
public interface Attributes {

    /**
     * 登录成功标志位
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
