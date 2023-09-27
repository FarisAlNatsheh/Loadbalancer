package entrypoint;

import ServerUtil.Endpoint;
import com.sun.net.httpserver.HttpServer;
import loadbalancer.Loadbalancer;
import loadbalancer.ReverseProxyHandler;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class Entrypoint {
    private String endpoint;
    private HttpServer server;

    private Loadbalancer loadbalancer;
    private int port;

    public Entrypoint(String endpoint, int port) {
        this.endpoint = endpoint;
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.println("Server could not be started");
            System.exit(1);
        }
    }
    public void startServer(List<Endpoint> endpointList, String path) {

        loadbalancer = new Loadbalancer(endpointList,0, endpoint);
        // Create a context for handling requests
        server.createContext("/"+path, new ReverseProxyHandler(loadbalancer));
        server.start();
        System.out.println("Server is running on port 8081");
    }
    public void stopServer() {
        server.removeContext("/");
        server.stop(0);
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
