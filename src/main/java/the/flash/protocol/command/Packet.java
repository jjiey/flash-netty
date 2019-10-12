package the.flash.protocol.command;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 通信过程中的对象
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    /**
     * 获取指令的抽象方法，所有的指令数据包都必须实现这个方法，这样就可以知道某种指令的含义
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
