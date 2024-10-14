package malenquilla.cds.common.grpc.clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public abstract class AbstractGrpcClient {
    private ManagedChannel channel;

    @PostConstruct
    public abstract void initStub();

    @PreDestroy
    private void destroy() throws InterruptedException {
        try {
            this.channel.shutdown();
            this.channel.awaitTermination(2, TimeUnit.MINUTES);
        } finally {
            this.channel.shutdownNow();
            this.channel = null;
        }
    }

    public AbstractGrpcClient initChanel(String host, int port) {
        if (this.channel != null)
            return this;

        this.channel = ManagedChannelBuilder.forAddress(host, port)
                                            .usePlaintext()
                                            .idleTimeout(10, TimeUnit.MINUTES)
                                            .enableRetry()
                                            .maxRetryAttempts(5)
                                            .build();
        return this;
    }

    public void refreshConnection() {
        if (this.channel != null && !this.channel.isShutdown())
            this.channel.resetConnectBackoff();
    }
}
