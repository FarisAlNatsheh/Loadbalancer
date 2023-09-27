package scaler;

import ServerUtil.Endpoint;
import ServerUtil.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scaler {
    private List<Endpoint> endpointList;
    private Map<String, Server> serverMap = new HashMap<>();
    private String image;

    public void getServersFromEndpoints() {
        for (int i = 0; i < endpointList.size(); i++) {
            serverMap.put(endpointList.get(i).getHostIP(),
                    new Server(endpointList.get(i).getHostIP(),
                            22,
                            endpointList.get(i).getPassword(),
                            endpointList.get(i).getUsername(),
                            image, new ArrayList<>(), 8999));


        }
    }


    public Scaler(List<Endpoint> endpointList, String image) {
        this.endpointList = endpointList;
        this.image = image;
        getServersFromEndpoints();
        for (String s : serverMap.keySet()) {

            int x = 0;
            while (true) {

                serverMap.get(s).checkContainers();

                if (x == 0)
                    serverMap.get(s).startReverseProxy();
//                else
//                    serverMap.get(s).restartReverseProxy();
                x = 1;
            }


            //System.out.println(ss);
        }
    }

}
