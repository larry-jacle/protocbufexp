package com.jacle.serialization;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * grpc客户端代码
 *
 */
public class SingleStreamClient
{
    public static final Logger logger = Logger.getLogger(SingleStreamClient.class.getName());

    private final ManagedChannel channel;
    private final GreeterSingleStreamGrpc.GreeterSingleStreamBlockingStub blockingStub;

    public SingleStreamClient(ManagedChannel channel) {
        this.channel = channel;
        // 获得stub
        this.blockingStub = GreeterSingleStreamGrpc.newBlockingStub(channel);
    }

    public SingleStreamClient(String host, int port) {
        // 创建channel
        this (ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
    }

    // 关闭channel
    public void shutdown () throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.MINUTES);
    }

    // 利用stub调用sayHello
    public void greet (String name) {
        logger.info("Will try to greet " + name + " ...");

        HelloRequestSingleStream request = HelloRequestSingleStream.newBuilder().setName(name).build();
        //客户端通过stub来调用具体的rpc服务方法
        //返回的是多个对象的时候，方法返回的是Iterator
         Iterator<HelloReplySingleStream> iterator= blockingStub.sayHello(request);
         while(iterator.hasNext())
         {
             logger.info("Greeting: " + iterator.next().getMessage());
         }

    }


    public static void main(String[] args) throws InterruptedException {
        SingleStreamClient client = new SingleStreamClient("localhost", 50052);
        try {
            client.greet("client-test unit");
        } finally {
            client.shutdown();
        }
    }


}
