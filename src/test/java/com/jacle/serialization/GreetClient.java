package com.jacle.serialization;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * grpc客户端代码
 */
public class GreetClient
{
    public static final Logger logger = Logger.getLogger(GreetService.class.getName());

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public GreetClient(ManagedChannel channel) {
        this.channel = channel;
        // 获得stub
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public GreetClient(String host, int port) {
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

        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        //客户端通过stub来调用具体的rpc服务方法
        HelloReply reply = blockingStub.sayHello(request);

        logger.info("Greeting: " + reply.getMessage());
    }


    public static void main(String[] args) throws InterruptedException {
        GreetClient client = new GreetClient("localhost", 50051);
        try {
            client.greet("world");
        } finally {
            client.shutdown();
        }
    }


}
