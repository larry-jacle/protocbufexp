package com.jacle.serialization;

import io.grpc.stub.StreamObserver;

/**
 * 服务端返回流的single service
 */
public class SingleStreamService  extends GreeterSingleStreamGrpc.GreeterSingleStreamImplBase
{
    //request就是请求对象
    @Override
    public void sayHello(HelloRequestSingleStream request, StreamObserver<HelloReplySingleStream> responseObserver) {
        //这里返回的是一个流，也就是多个对象
        //构造响应对象
        for(int i=0;i<10;i++)
        {
            HelloReplySingleStream reply= HelloReplySingleStream.newBuilder().setMessage(request.getName()+"-->msg:"+i).build();
            responseObserver.onNext(reply);
        }

        responseObserver.onCompleted();
    }
}
