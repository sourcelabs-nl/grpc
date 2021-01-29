package nl.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 8080)
            .usePlaintext()
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc
            .newBlockingStub(channel)
//            .withDeadlineAfter(1200, TimeUnit.MILLISECONDS)
            .withCallCredentials(new SlabsCredentials());

        doCallHello(stub, "Rick");
        doCallHello(stub, "Francois");
        doCallHello(stub, "Daniel");
//        doCallHello(stub, "Long");

        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver observer = new StreamObserver<HelloResponse>() {

            @Override
            public void onNext(HelloResponse value) {
                System.out.println(value.toString());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
                latch.countDown();
            }
        };

        HelloServiceGrpc.HelloServiceStub streamStub = HelloServiceGrpc
            .newStub(channel)
            .withCallCredentials(new SlabsCredentials());

        streamStub.hellostream(HelloRequest.newBuilder().setFirstName("Jarno").setLastName("Walgemoed").build(), observer);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.shutdown();

    }

    private static void doCallHello(HelloServiceGrpc.HelloServiceBlockingStub stub, String aLong) {
        try {
            HelloResponse response = stub.hello(HelloRequest.newBuilder().setFirstName(aLong).setLastName("Walgemoed").build());
            System.out.println(response);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
