package nl.example.grpc;

import io.grpc.*;

import java.io.IOException;

public class TheServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        io.grpc.Server server = ServerBuilder
            .forPort(8080)
            .addService(new HelloServiceImpl())
            .intercept(new ServerInterceptor() {
                @Override
                public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                    ServerCall<ReqT, RespT> serverCall,
                    Metadata metadata,
                    ServerCallHandler<ReqT, RespT> serverCallHandler
                ) {
                    if (metadata.containsKey(Metadata.Key.of("Authentication", Metadata.ASCII_STRING_MARSHALLER))) {
                        String authentication = metadata.get(Metadata.Key.of("Authentication", Metadata.ASCII_STRING_MARSHALLER));
                        if ("SLOT_BOY".equals(authentication)) {
                            return Contexts.interceptCall(Context.current(), serverCall, metadata, serverCallHandler);
                        }
                    }
                    serverCall.close(Status.PERMISSION_DENIED, metadata);
                    return new ServerCall.Listener<>() {
                    };
                }
            })
            .build();

        server.start();
        System.out.println("server started");
        server.awaitTermination();
    }
}


