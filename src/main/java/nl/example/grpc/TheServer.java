package nl.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class TheServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        io.grpc.Server server = ServerBuilder.forPort(8080).addService(new HelloServiceImpl()).build();
        server.start();
        server.awaitTermination();
    }
}
