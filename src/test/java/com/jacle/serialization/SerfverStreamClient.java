package com.jacle.serialization;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * grpc客户端代码
 *
 */
public class SerfverStreamClient
{
    public static final Logger logger = Logger.getLogger(SerfverStreamClient.class.getName());

    private final ManagedChannel channel;

    //定义异步的stub
    private final GreeterServerStreamGrpc.GreeterServerStreamStub stub;

    public SerfverStreamClient(ManagedChannel channel) {
        this.channel = channel;
        // 获得stub
        this.stub = GreeterServerStreamGrpc.newStub(channel);
    }

    public SerfverStreamClient(String host, int port) {
        // 创建channel
        this (ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
    }

    // 关闭channel
    public void shutdown () throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.MINUTES);
    }

    // 利用stub调用sayHello
    public CountDownLatch greet (String name) throws InterruptedException {

        final CountDownLatch countDownLatch=new CountDownLatch(1);

        //客户端通过stub来调用远程方法
        StreamObserver<HelloReplyServerStream> streamStreamObserver=new StreamObserver<HelloReplyServerStream>() {
            @Override
            public void onNext(HelloReplyServerStream helloReplyServerStream) {
                //接收返回结果的观察者
                logger.info(helloReplyServerStream.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                //发送错误，停止等待响应，直接运行程序到结尾
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                //显示响应之后，主程序继续执行
                countDownLatch.countDown();
            }
        };

        //stub调用方法
        //调用的方法有回调函数，两者之间有顺序关系，先发送了消息之后，等待callback给出返回结果
        StreamObserver<HelloRequestServerStream> clientObserver=stub.sayHello(streamStreamObserver);

        //发送消息
        clientObserver.onNext(HelloRequestServerStream.newBuilder().setName("client_send_1").build());
        clientObserver.onNext(HelloRequestServerStream.newBuilder().setName("client_send_2").build());
        clientObserver.onNext(HelloRequestServerStream.newBuilder().setName("client_send_3").build());
        clientObserver.onCompleted();

        return countDownLatch;
    }


    public static void main(String[] args) throws InterruptedException {
        SerfverStreamClient client = new SerfverStreamClient("localhost", 50052);
        try {
            CountDownLatch countDownLatch=client.greet("client-test unit");

//            countDownLatch.await(1,TimeUnit.MINUTES);
            countDownLatch.await();
        } finally {
            client.shutdown();
        }

    }


}
