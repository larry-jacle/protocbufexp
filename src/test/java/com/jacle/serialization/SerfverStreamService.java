package com.jacle.serialization;

import io.grpc.stub.StreamObserver;

/**
 * 客户端的参数是流的形式
 * 多个输入request，一个reply
 */
public class SerfverStreamService extends GreeterServerStreamGrpc.GreeterServerStreamImplBase
{
    //request就是请求对象
    //返回的是StreamObserver

    @Override
    public StreamObserver<HelloRequestServerStream> sayHello(StreamObserver<HelloReplyServerStream> responseObserver) {
        //返回一个reply
        return new StreamObserver<HelloRequestServerStream>() {
            private StringBuffer buffer=new StringBuffer();

            @Override
            public void onNext(HelloRequestServerStream helloRequestServerStream) {
                //处理多个请求信息的内容
                buffer.append(helloRequestServerStream.getName()+"--");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                //发送reply对象
                responseObserver.onNext(HelloReplyServerStream.newBuilder().setMessage(buffer.toString()).build());
                responseObserver.onCompleted();
            }
        };

    }
}
