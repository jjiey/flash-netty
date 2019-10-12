package the.flash.protocol.command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import the.flash.serialize.Serializer;
import the.flash.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

import static the.flash.protocol.command.Command.LOGIN_REQUEST;

/**
 * 把数据编码到通信协议的二进制数据包中
 */
public class PacketCodeC {

    /**
     * 魔数
     */
    private static final int MAGIC_NUMBER = 0x12345678;
    /**
     * K-指令，V-通信对象
     */
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    /**
     * K-序列化算法标识，V-序列化算法
     */
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
    }

    /**
     * 封装成二进制
     */
    public ByteBuf encode(Packet packet) {
        // 1. 创建 ByteBuf 对象
        // ioBuffer()方法会返回适配io读写相关的内存，它会尽可能创建一个直接内存，直接内存可以理解为不受jvm堆管理的内存空间，写到IO缓冲区的效果更高
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解析二进制
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number todo 校验魔数
        byteBuf.skipBytes(4);
        // 跳过版本号 暂不处理
        byteBuf.skipBytes(1);
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        // 根据拿到的数据包的长度取出数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
