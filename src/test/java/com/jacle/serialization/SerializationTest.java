package com.jacle.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;


/**
 * protobuf的序列化和反序列化
 */
public class SerializationTest
{
    @Test
    public void testProtobuf() throws InvalidProtocolBufferException {
        //根据外部类来创建内部类的构造方法
        //通过生成的class文件直接可以进行引用，而不需要导入java文件
        FirstDemo.Demo.Builder demoBuilder=FirstDemo.Demo.newBuilder();
        demoBuilder.setEmail("jijj19@163.com");
        demoBuilder.setName("jacle");
        FirstDemo.Demo demo=demoBuilder.build();

        System.out.println(demo.toString());
        System.out.println("after serialization:");
        byte[] serialBytes=demo.toByteArray();
        for(byte b:serialBytes)
        {
            System.out.print(b);
            System.out.print(" ");
        }

        System.out.println("-------deserialization:-------");
        //序列化数据反序列化
        FirstDemo.Demo deSerialDemo= FirstDemo.Demo.parseFrom(serialBytes);
        System.out.println(deSerialDemo);


    }
}
