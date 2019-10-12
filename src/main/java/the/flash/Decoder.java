package the.flash;

/**
 * @author yangjie
 * @date Created in 2019/10/12 19:58
 * @description 拆包器
 */
public class Decoder {

    public static void main(String[] args) {
        /**
         * FixedLengthFrameDecoder：固定长度的拆包器
         * 如果你的应用层协议非常简单，每个数据包的长度都是固定的，比如100，那么只需要把这个拆包器加到pipeline中，Netty会把一个个长度为100的数据包(ByteBuf)传递到下一个channelHandler
         */
        /**
         * LineBasedFrameDecoder：行拆包器
         * 从字面意思来看，发送端发送数据包的时候，每个数据包之间以换行符作为分隔，接收端通过LineBasedFrameDecoder将粘过的ByteBuf拆分成一个个完整的应用层数据包
         */
        /**
         * DelimiterBasedFrameDecoder：分隔符拆包器
         * 行拆包器的通用版本，只不过我们可以自定义分隔符
         */
        /**
         * LengthFieldBasedFrameDecoder：基于长度域拆包器
         * 最通用的一种拆包器，只要你的自定义协议中包含长度域字段，均可以使用这个拆包器来实现应用层拆包。
         * 由于上面三种拆包器比较简单，读者可以自行写出demo，接下来，我们就结合代码中的自定义协议，来学习一下如何使用基于长度域的拆包器来拆解我们的数据包
         */
    }

}
