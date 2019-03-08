package com.jacle.serialization;


import io.grpc.stub.StreamObserver;

/**
 * 服务端逻辑的实现
 * 复写  rpc的具体方法
 */
public class GreetImpl  extends GreeterGrpc.GreeterImplBase
{
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply helloReply= HelloReply.newBuilder().setMessage("hello reply,method param:"+request.getName()).build();

        //响应
        responseObserver.onNext(helloReply);
        //结束
        responseObserver.onCompleted();

    }
}
