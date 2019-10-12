package the.flash.serialize;

import the.flash.serialize.impl.JSONSerializer;

/**
 * 序列化接口
 */
public interface Serializer {

    /**
     * 默认序列化算法
     */
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法，获取具体的序列化算法标识
     * @return
     */
    byte getSerializerAlogrithm();

    /**
     * java对象转换成二进制字节数组
     */
    byte[] serialize(Object object);

    /**
     * 二进制字节数组转换成java对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}
