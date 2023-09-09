package faris.loadbalancer;
import ServerUtil.Service;
import com.sun.net.httpserver.HttpServer;
import entrypoint.Entrypoint;
import loadbalancer.Loadbalancer;
import loadbalancer.ReverseProxyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {



    public static void main(String[] args) throws IOException {
        List<Service> serviceList = new ArrayList<>();
        serviceList.add(new Service("localhost", 8080));
        new Entrypoint().startServer(serviceList, "");
        // Create an HTTP server that listens on port 8080

    }
}