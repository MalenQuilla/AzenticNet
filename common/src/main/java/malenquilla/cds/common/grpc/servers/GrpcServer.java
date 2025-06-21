package malenquilla.cds.common.grpc.servers;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class GrpcServer extends Server {
    private static int PORT;
    private final static Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private final List<ServerServiceDefinition> controllers;

    private Server server;
    private boolean isRunning = false;

    @Value("${cds.grpc.server.port}")
    public void setPort(int port) {
        GrpcServer.PORT = port;
    }

    @PostConstruct
    private void buildAndStart() {
        this.server = ServerBuilder.forPort(PORT)
                                   .addServices(controllers)
                                   .intercept(new GrpcServerExceptionInterceptor())
                                   .build();

        this.start();
    }

    @PreDestroy
    public void destroy() {
        this.shutdown();
    }

    @Override
    public GrpcServer start() {
        if (this.isRunning) return this;

        this.isRunning = true;
        try {
            this.server.start();

            logger.info(String.format(
                "Server started on port %d. Number of controllers: %d", this.getPort(), this.getControllersCount()));
        } catch (IOException exception) {
            this.isRunning = false;

            logger.warning("Grpc server initialized failed");
        }

        return this;
    }

    public long getControllersCount() {
        return this.server.getServices()
                          .size();
    }

    @Override
    public int getPort() {
        return this.server.getPort();
    }

    @Override
    public GrpcServer shutdown() {
        if (!this.isRunning) return this;

        this.isRunning = false;
        this.server.shutdown();

        try {
            this.server.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException exception) {
            logger.warning("Grpc server await termination failed, force shutdown now");
            this.server.shutdownNow();
        }

        logger.info("Grpc server shutting down...");

        return this;
    }

    @Override
    public Server shutdownNow() {
        return this.server.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.server.isShutdown();
    }

    public void restart() {
        this.shutdown()
            .start();
    }

    @Override
    public boolean isTerminated() {
        return !this.isRunning;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.server.awaitTermination(timeout, unit);
    }

    @Override
    public void awaitTermination() throws InterruptedException {
        this.server.awaitTermination();
    }
}
