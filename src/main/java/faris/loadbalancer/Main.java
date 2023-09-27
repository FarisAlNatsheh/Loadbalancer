package faris.loadbalancer;
import ServerUtil.Endpoint;
import entrypoint.Entrypoint;
import scaler.Scaler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        List<Endpoint> endpointList = new ArrayList<>();
        endpointList.add(new Endpoint("192.168.1.19", 8080,"12123","faris"));
//        new Entrypoint("login",8081).startServer(endpointList, "");
//        new Entrypoint("login",8082).startServer(endpointList, "login")
//        new Scaler(endpointList,"farisalnatsheh/login:1.0.1");
        new Scaler(endpointList,"nginx");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Code to be executed when the program is exiting
            System.out.println("Java program is exiting. Performing cleanup...");
            // Add your cleanup code here
        }));
    }
}