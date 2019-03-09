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
public class AllStreamClient
{
    public static final Logger logger = Logger.getLogger(AllStreamClient.class.getName());

    private final ManagedChannel channel;

    //定义异步的stub
    private final GreeterAllStreamGrpc.GreeterAllStreamStub stub;

    public AllStreamClient(ManagedChannel channel) {
        this.channel = channel;
        // 获得stub
        this.stub = GreeterAllStreamGrpc.newStub(channel);
    }

    public AllStreamClient(String host, int port) {
        // 创建channel
        this (ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
    }

    // 关闭channel
    public void shutdown () throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.MINUTES);
    }

    // 利用stub调用rpc的服务
    public CountDownLatch greet (String name) throws InterruptedException {

        final CountDownLatch countDownLatch=new CountDownLatch(1);

        //客户端通过stub来调用远程方法
        StreamObserver<AllStreamReply> streamStreamObserver=new StreamObserver<AllStreamReply>() {
            @Override
            public void onNext(AllStreamReply allStreamReply) {
                //显示返回的信息
                System.out.println(System.currentTimeMillis()+":"+allStreamReply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
               countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
               countDownLatch.countDown();
            }
        };

        //stub调用方法
        //调用的方法有回调函数，两者之间有顺序关系，先发送了消息之后，等待callback给出返回结果
        StreamObserver<AllStreamRequest> clientObserver=stub.sayAllHello(streamStreamObserver);

        //发送消息
        clientObserver.onNext(AllStreamRequest.newBuilder().setName("AllStream_client_send_1").build());
        clientObserver.onNext(AllStreamRequest.newBuilder().setName("AllStream_client_send_2").build());
        clientObserver.onNext(AllStreamRequest.newBuilder().setName("AllStream_client_send_3").build());
        clientObserver.onCompleted();

        return countDownLatch;
    }


    public static void main(String[] args) throws InterruptedException {
        AllStreamClient client = new AllStreamClient("localhost", 50052);
        try {
            CountDownLatch countDownLatch=client.greet("all Stream client-test unit");

//            countDownLatch.await(1,TimeUnit.MINUTES);
            countDownLatch.await();
        } finally {
            client.shutdown();
        }

    }


}
