package the.flash.protocol.response;

import lombok.Data;
import the.flash.protocol.Packet;

import java.util.List;

import static the.flash.protocol.command.Command.CREATE_GROUP_RESPONSE;

/**
 * 创建群聊响应数据包
 */
@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    /**
     * 群聊id
     */
    private String groupId;

    /**
     * 群聊成员列表
     */
    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}
