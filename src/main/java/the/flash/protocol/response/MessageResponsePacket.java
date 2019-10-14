package the.flash.protocol.response;

import lombok.Data;
import the.flash.protocol.Packet;

import static the.flash.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * 服务端要发送的消息对象
 */
@Data
public class MessageResponsePacket extends Packet {

    /**
     * 发送消息方的用户标识
     */
    private String fromUserId;

    /**
     * 发送消息方的用户昵称
     */
    private String fromUserName;

    /**
     * 发送消息方的用户消息内容
     */
    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
