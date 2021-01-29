package nl.example.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = "Hello " + request.getFirstName() + " " + request.getLastName();

        System.out.println(greeting);
        HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();

        responseObserver.onNext(response);

        if (request.getFirstName().equals("Rick")) {
            Status status = Status.INVALID_ARGUMENT.withDescription("Deze naam bestaat niet");
            responseObserver.onError(status.asRuntimeException());
        } else if (request.getFirstName().equals("Francois")) {
            responseObserver.onError(new ClassNotFoundException());
        } else if (request.getFirstName().equals("Daniel")) {
            responseObserver.onError(new IllegalArgumentException("Daniel zijn account is blocked"));
        } else if (request.getFirstName().equals("Long")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Status status = Status.NOT_FOUND.withDescription("Super lange request");
            responseObserver.onError(status.asRuntimeException());
        } else {
            responseObserver.onCompleted();
        }

    }

    @Override
    public void hellostream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = "Hello " + request.getFirstName() + " " + request.getLastName();

        HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();

        responseObserver.onNext(response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
