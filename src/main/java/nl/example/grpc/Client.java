package nl.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class Client {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
        HelloResponse response = stub.hello(HelloRequest.newBuilder().setFirstName("Jarno").setLastName("Walgemoed").build());

        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver observer = new StreamObserver<HelloResponse>() {

            @Override
            public void onNext(HelloResponse value) {
                System.out.println(value.toString());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        HelloServiceGrpc.HelloServiceStub streamStub = HelloServiceGrpc.newStub(channel);
        streamStub.hellostream(HelloRequest.newBuilder().setFirstName("Jarno").setLastName("Walgemoed").build(), observer);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.shutdown();

    }
}
