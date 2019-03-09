package com.jacle.serialization;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端是流响应
 */
public class AllStreamImpl extends  GreeterAllStreamGrpc.GreeterAllStreamImplBase
{
    private List<String> list=new ArrayList<String>();

    @Override
    public StreamObserver<AllStreamRequest> sayAllHello(StreamObserver<AllStreamReply> responseObserver) {

        return new StreamObserver<AllStreamRequest>()
        {
            @Override
            public void onNext(AllStreamRequest allStreamRequest) {
                //处理客户端发送的数据
                list.add(allStreamRequest.getName()+"--reply");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                for(String s:list)
                {
                    responseObserver.onNext(AllStreamReply.newBuilder().setMessage(s).build());
                }
                responseObserver.onCompleted();
            }
        };
    }
}
