package entrypoint;

import ServerUtil.Service;
import com.sun.net.httpserver.HttpServer;
import loadbalancer.Loadbalancer;
import loadbalancer.ReverseProxyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class Entrypoint {
    public void startServer(List<Service> serviceList, String path) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Create a context for handling requests
        server.createContext("/"+path, new ReverseProxyHandler(new Loadbalancer(serviceList,0, "login")));
        server.start();
        System.out.println("Server is running on port 8081");
    }
}
