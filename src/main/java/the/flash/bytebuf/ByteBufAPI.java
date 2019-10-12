package the.flash.bytebuf;

/**
 * @author yangjie
 * @date Created in 2019/10/12 14:29
 * @description API
 */
public class ByteBufAPI {

    public static void main(String[] args) {
        /* 1.容量API */
        // capacity()：表示ByteBuf底层占用了多少字节的内存（包括丢弃的字节、可读字节、可写字节），不同的底层实现机制有不同的计算方式
        // maxCapacity()：表示ByteBuf底层最大能够占用多少字节的内存，当向ByteBuf中写数据的时候，如果发现容量不足，则进行扩容，直到扩容到maxCapacity，超过这个数，就抛异常
        // readableBytes()：表示ByteBuf当前可读的字节数，它的值等于writerIndex-readerIndex，如果两者相等，则不可读
        // isReadable()：是否可读
        // writableBytes()：表示ByteBuf当前可写的字节数，它的值等于capacity-writerIndex，如果两者相等，则表示不可写，isWritable()返回false，但是这个时候，并不代表不能往ByteBuf中写数据了，如果发现往ByteBuf中写数据写不进去的话，Netty会自动扩容ByteBuf，直到扩容到底层的内存大小为maxCapacity
        // isWritable()：是否可写
        // maxWritableBytes()：表示可写的最大字节数，它的值等于maxCapacity-writerIndex

        /* 2.读写指针相关的API */
        // readerIndex()：表示返回当前的读指针readerIndex
        // readerIndex(int)：表示设置读指针
        // writeIndex()：表示返回当前的写指针writerIndex
        // writeIndex(int)：表示设置写指针
        // markReaderIndex()：表示把当前的读指针保存起来
        // resetReaderIndex()：表示把当前的读指针恢复到之前保存的值
        /*
        下面两段代码是等价的
        // 代码片段1
        int readerIndex = buffer.readerIndex();
        // .. 其他操作
        buffer.readerIndex(readerIndex);

        // 代码片段二
        buffer.markReaderIndex();
        // .. 其他操作
        buffer.resetReaderIndex();
        推荐使用代码片段二方式，不需要自己定义变量，无论buffer当作参数传递到哪里，调用resetReaderIndex()都可以恢复到之前的状态，在解析自定义协议的数据包的时候非常常见
         */
        // markWriterIndex()：表示把当前的写指针保存起来
        // resetWriterIndex()：表示把当前的写指针恢复到之前保存的值

        /* 3.读写API，本质上，关于ByteBuf的读写都可以看作从指针开始的地方开始读写数据 */
        // writeBytes(byte[] src)：表示把字节数组src里面的数据全部写到 ByteBuf，这里src字节数组大小的长度通常小于等于writableBytes()（ByteBuf当前可写的字节数）
        // buffer.readBytes(byte[] dst)：表示把ByteBuf里面的数据全部读取到dst，这里dst字节数组的大小通常等于readableBytes()（ByteBuf当前可读的字节数）
        // writeByte(byte b)：表示往ByteBuf中写一个字节
        // buffer.readByte()：表示从ByteBuf中读取一个字节
        // 类似writeByte(byte b)与buffer.readByte()的API还有：writeBoolean()、writeChar()、writeShort()、writeInt()、writeLong()、writeFloat()、writeDouble() 与 readBoolean()、readChar()、readShort()、readInt()、readLong()、readFloat()、readDouble()
        // 与读写API类似的API还有getBytes、getByte()与setBytes()、setByte()系列，唯一的区别就是get/set不会改变读写指针，而read/write会改变读写指针，这点在解析数据的时候千万要注意
        // release()：将ByteBuf引用计数减一
        // retain()：将ByteBuf引用计数加一
        /*
        由于Netty使用了堆外内存，而堆外内存是不被jvm直接管理的，也就是说申请到的内存无法被垃圾回收器直接回收，所以需要我们手动回收。有点类似于c语言里面，申请到的内存必须手工释放，否则会造成内存泄漏。
        Netty的ByteBuf是通过引用计数的方式管理的，如果一个ByteBuf没有地方被引用到，需要回收底层内存。默认情况下，当创建完一个ByteBuf，它的引用为1，然后每次调用retain()方法，它的引用就加一，release()方法原理是将引用计数减一，减完之后如果发现引用计数为0，则直接回收ByteBuf底层的内存。
         */
        // slice()：返回值是一个新的ByteBuf对象。从原始ByteBuf中截取一段，这段数据是从readerIndex到writeIndex（可读字节），同时，返回的新的ByteBuf的最大容量maxCapacity为原始ByteBuf的readableBytes()（ByteBuf当前可读的字节数）。不会拷贝数据，只是通过改变读写指针来改变读写的行为
        // duplicate()：返回值是一个新的ByteBuf对象。把整个ByteBuf都截取出来，包括所有的数据，指针信息。不会拷贝数据，只是通过改变读写指针来改变读写的行为
        // copy()返回值是一个新的ByteBuf对象。从原始的ByteBuf中拷贝所有的信息，包括读写指针以及底层对应的数据，因此，copy()返回的ByteBuf中写数据不会影响到原始的ByteBuf
        /*
        slice()方法与duplicate()方法的相同点：底层内存以及引用计数与原始的 ByteBuf共享，也就是说经过slice()或者duplicate()返回的ByteBuf调用write系列方法都会影响到原始的ByteBuf，但是它们都维持着与原始ByteBuf相同的内存引用计数和不同的读写指针
        slice()方法与duplicate()方法的不同点：slice()只截取从readerIndex到writerIndex之间的数据，它返回的ByteBuf的最大容量被限制到原始ByteBuf的readableBytes(), 而duplicate()是把整个ByteBuf都与原始的ByteBuf共享
        slice()方法与duplicate()方法不会拷贝数据，它们只是通过改变读写指针来改变读写的行为
        slice()和duplicate()不会改变ByteBuf的引用计数，所以原始的ByteBuf调用release()之后发现引用计数为零，就开始释放内存，调用这两个方法返回的ByteBuf也会被释放，这个时候如果再对它们进行读写，就会报错。因此，我们可以通过调用一次retain()方法来增加引用，表示它们对应的底层的内存多了一次引用，引用计数为2，在释放内存的时候，需要调用两次release()方法，将引用计数降到零，才会释放内存
        这三个方法均维护着自己的读写指针，与原始的ByteBuf的读写指针无关，相互之间不受影响
         */
        // retainedSlice()：在截取内存片段的同时，增加内存的引用计数。retainedSlice()等价于slice().retain();
        // retainedDuplicate()：在截取内存片段的同时，增加内存的引用计数。retainedDuplicate()等价于duplicate().retain()
        /*
        使用到slice和duplicate方法的时候，千万要理清内存共享，引用计数共享，读写指针不共享几个概念
        下面举两个常见的易犯错的例子：1.多次释放；2.不释放造成内存泄漏
        1.多次释放
        Buffer buffer = xxx;
        doWith(buffer);
        // 一次释放
        buffer.release();

        public void doWith(Bytebuf buffer) {
            // ...
            // 没有增加引用计数
            Buffer slice = buffer.slice();
            foo(slice);
        }

        public void foo(ByteBuf buffer) {
            // read from buffer
            // 重复释放
            buffer.release();
        }
        这里的doWith有的时候是用户自定义的方法，有的时候是Netty的回调方法，比如channelRead()等等
        2.不释放造成内存泄漏
        Buffer buffer = xxx;
        doWith(buffer);
        // 引用计数为2，调用release方法之后，引用计数为1，无法释放内存
        buffer.release();

        public void doWith(Bytebuf buffer) {
            // ...
            // 增加引用计数
            Buffer slice = buffer.retainedSlice();
            foo(slice);
            // 没有调用 release
        }

        public void foo(ByteBuf buffer) {
            // read from buffer
        }
        为了避免以上两种情况发生，一定要记得一点：遵循谁retain()谁release()的原则，在一个函数体里面，只要增加了引用计数（包括ByteBuf的创建或手动调用retain()方法），就必须调用release()方法
         */
    }

}
