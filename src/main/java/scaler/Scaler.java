package scaler;

import ServerUtil.Endpoint;
import ServerUtil.Server;
import entrypoint.Entrypoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scaler {
    private List<Endpoint> endpointList;
    private Map<String, Server> serverMap = new HashMap<>();
    private String image;
    private Entrypoint mainPoint = new Entrypoint("", 8081);

    public void getServersFromEndpoints() {
        for (int i = 0; i < endpointList.size(); i++) {
            serverMap.put(endpointList.get(i).getHostIP(),
                    new Server(endpointList.get(i).getHostIP(),
                            22,
                            endpointList.get(i).getPassword(),
                            endpointList.get(i).getUsername(),
                            image, new ArrayList<>(), 9000, mainPoint));


        }
    }

    public void addExitHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Exiting...");
            for (String s : serverMap.keySet()) {
                serverMap.get(s).sendCommand("echo " + serverMap.get(s).getPass() + " | sudo -S docker kill $(echo " + serverMap.get(s).getPass() + "| sudo -S docker ps -q)");
            }

        }));
    }

    public Scaler(List<Endpoint> endpointList, String image) {
        addExitHook();
        this.endpointList = endpointList;
        this.image = image;
        getServersFromEndpoints();
        int x = 0;
        List<Endpoint> containers = new ArrayList<>();
        this.endpointList = new ArrayList<>();
        startReverseProxy();
        while (true) {
            List<Endpoint> containersCopy = new ArrayList<>();
            for (int j = 0; j < 1; j++)
                for (String s : serverMap.keySet()) {
                    serverMap.get(s).checkContainers();
                    List<String> serverContainers = serverMap.get(s).getContainers();
                    for (int i = 0; i < serverContainers.size(); i++) {
                        containersCopy.add(new Endpoint(serverMap.get(s).getIp(), serverMap.get(s).getStartingPort() - i - 1, serverMap.get(s).getPass(), serverMap.get(s).getUser()));
                    }
                }
            if (!containersCopy.equals(containers)) {
                containers = containersCopy;
//                List<Endpoint> listWithoutDuplicates = containers.stream()
//                        .distinct()
//                        .collect(Collectors.toList());
                List<Endpoint> listWithoutDuplicates = removeDuplicateEndpoints(containers);
                restartReverseProxy(listWithoutDuplicates);
            }
        }

    }

    public void startReverseProxy() {
        // addExitHook();
        //n.stopServer();
        mainPoint.startServer(endpointList, "");
    }

    public void restartReverseProxy(List<Endpoint> containers) {
        mainPoint.stopServer();

        mainPoint.startServer(containers, "");
    }

    public static List<Endpoint> removeDuplicateEndpoints(List<Endpoint> inputList) {
        List<Endpoint> listWithoutDuplicates = new ArrayList<>();

        for (Endpoint endpoint : inputList) {
            if (!containsEndpoint(listWithoutDuplicates, endpoint)) {
                listWithoutDuplicates.add(endpoint);
            }
        }

        return listWithoutDuplicates;
    }

    private static boolean containsEndpoint(List<Endpoint> list, Endpoint endpoint) {
        for (Endpoint e : list) {
            if (e.getHostIP().equals(endpoint.getHostIP()) && e.getPort() == endpoint.getPort()) {
                return true;
            }
        }
        return false;
    }


}
