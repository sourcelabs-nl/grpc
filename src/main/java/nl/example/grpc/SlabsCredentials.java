package nl.example.grpc;

import io.grpc.Attributes;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import java.util.concurrent.Executor;

public class SlabsCredentials implements CallCredentials {

    @Override
    public void applyRequestMetadata(MethodDescriptor<?, ?> methodDescriptor, Attributes attributes, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            Metadata headers = new Metadata();
            headers.put(Metadata.Key.of("Authentication", Metadata.ASCII_STRING_MARSHALLER), "SLOT_BOY");
            metadataApplier.apply(headers);
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }
}
