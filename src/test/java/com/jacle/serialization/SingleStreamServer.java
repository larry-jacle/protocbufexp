package com.jacle.serialization;


import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * grpc服务端代码
 */
public class SingleStreamServer
{
    private static final Logger logger = Logger.getLogger(SingleStreamServer.class.getName());
    private static final int port = 50052;

    private Server server;

    private void start () throws IOException {
        server = ServerBuilder.forPort(port).addService(new SingleStreamService()).build().start();

        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("*** shutting down gRPC server since JVM is shutting down");
                SingleStreamServer.this.stop();
                System.out.println("*** server shut down");
            }
        });
    }

    // 停止服务
    private void stop () {
        if (server != null) {
            server.shutdown();
        }
    }

    // 等待主线程结束
    private void blockUntilShutdown () throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        SingleStreamServer greetService=new SingleStreamServer();
        greetService.start();
        greetService.blockUntilShutdown();

    }
}

