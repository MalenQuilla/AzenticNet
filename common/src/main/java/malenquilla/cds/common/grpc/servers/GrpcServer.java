package malenquilla.cds.common.grpc.servers;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class GrpcServer {
    private static int PORT;
    private final static Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private final List<BindableService> controllers;

    private Server server;
    private boolean isRunning = false;

    @Value("${cds.grpc.server.port}")
    public void setPort(int port) {
        GrpcServer.PORT = port;
    }

    @PostConstruct
    private void buildAndStart() {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(PORT);
        controllers.forEach(serverBuilder::addService);
        this.server = serverBuilder.build();

        this.start();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        this.shutdown();
    }

    private void start() {
        if (this.isRunning)
            return;

        this.isRunning = true;
        try {
            this.server.start();

            logger.info("Grpc server initialized on port " + this.getPort() + ". Number of controllers: " + this.getControllersCount());
        } catch (IOException exception) {
            this.isRunning = false;

            logger.warning("Grpc server initialized failed");
        }
    }

    public long getControllersCount() {
        return this.server.getServices().size();
    }

    public long getPort() {
        return this.server.getPort();
    }

    public void shutdown() throws InterruptedException {
        if (!this.isRunning)
            return;

        this.isRunning = false;
        this.server.shutdown();
        this.server.awaitTermination(2, TimeUnit.MINUTES);
        this.server.shutdownNow();

        logger.info("Grpc server shutting down...");
    }

    public void restart() throws InterruptedException {
        if (this.isRunning)
            this.shutdown();

        this.start();
    }

    public boolean isTerminated() {
        return !this.isRunning;
    }
}
