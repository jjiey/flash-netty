package the.flash.protocol.request;

import lombok.Data;
import the.flash.protocol.Packet;

import java.util.List;

import static the.flash.protocol.command.Command.CREATE_GROUP_REQUEST;

/**
 * 创建群聊请求数据包
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    /**
     * 需要拉取群聊的用户列表
     */
    private List<String> userIdList;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
