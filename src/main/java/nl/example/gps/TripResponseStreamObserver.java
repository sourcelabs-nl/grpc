package nl.example.gps;

import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.gps.TripRequest;
import com.vinsguru.gps.TripResponse;

import io.grpc.stub.StreamObserver;

public class TripResponseStreamObserver implements StreamObserver<TripResponse> {

    private StreamObserver<TripRequest> requestStreamObserver;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    @Override
    public void onNext(TripResponse tripResponse) {
        if(tripResponse.getRemainingDistance() > 0){
            print(tripResponse);
            this.drive();
        }else{
            this.requestStreamObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println("Trip Completed");
        countDownLatch.countDown();
    }

    public void startTrip(StreamObserver<TripRequest> requestStreamObserver){
        this.requestStreamObserver = requestStreamObserver;
        this.drive();
    }

    private void drive(){
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        TripRequest tripRequest = TripRequest.newBuilder().setDistanceTravelled(ThreadLocalRandom.current().nextInt(1, 10)).build();
        requestStreamObserver.onNext(tripRequest);
    }

    private void print(TripResponse tripResponse){
        System.out.println(LocalTime.now() + ": Remaining Distance : " + tripResponse.getRemainingDistance());
        System.out.println(LocalTime.now() + ": Time To Reach (sec): " + tripResponse.getTimeToDestination());
        System.out.println("------------------------------");
    }

}