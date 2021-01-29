import java.util.concurrent.TimeUnit;

import nl.example.gps.TripResponseStreamObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vinsguru.gps.NavigationServiceGrpc;
import com.vinsguru.gps.TripRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BiDirectionalStreamingTest {

    private ManagedChannel channel;

    private NavigationServiceGrpc.NavigationServiceStub clientStub;

    @Before
    public void setup() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        this.clientStub = NavigationServiceGrpc.newStub(channel).withDeadlineAfter(5, TimeUnit.MINUTES);
    }

    @After
    public void teardown() throws InterruptedException {
        this.channel.shutdown();
    }

    @Test
    public void tripTest() throws InterruptedException {
        TripResponseStreamObserver tripResponseStreamObserver = new TripResponseStreamObserver();
        StreamObserver<TripRequest> requestStreamObserver = this.clientStub.navigate(tripResponseStreamObserver);
        tripResponseStreamObserver.startTrip(requestStreamObserver);
        tripResponseStreamObserver.getCountDownLatch().await();
    }

}

