package nl.example.grpc;

import io.grpc.*;

import java.io.IOException;

import nl.example.gps.NavigationService;

public class TheServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        io.grpc.Server server = ServerBuilder
            .forPort(8080)
            //.addService(new HelloServiceImpl())
            .addService(new NavigationService())
            .build();

        server.start();
        System.out.println("server started");


        server.awaitTermination();
    }
}


