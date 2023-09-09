package faris.loadbalancer;
import ServerUtil.SSHCommandExecutor;
import ServerUtil.Server;
import com.sun.net.httpserver.HttpServer;
import loadbalancer.Loadbalancer;
import loadbalancer.ReverseProxyHandler;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {



    public static void main(String[] args) throws IOException {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server("localhost","faris","12123", 8080, "login"));

        // Create an HTTP server that listens on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Create a context for handling requests
        server.createContext("/", new ReverseProxyHandler(new Loadbalancer(serverList,0)));
        //new SSHCommandExecutor("192.168.1.80","faris","12123",22).executeCommand("ls -al");
        // Start the server
        server.start();
        System.out.println("Server is running on port 8081");
    }
}