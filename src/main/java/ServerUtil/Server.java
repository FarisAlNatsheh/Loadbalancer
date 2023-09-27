package ServerUtil;

import entrypoint.Entrypoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Server {
    private final int STARTING_PORT=9000;
    private final int MAX_CONTAINERS=5;
    private final int MAX_USAGE=15;

    @NonNull
    private String ip;
    @NonNull
    private int port;
    @NonNull
    private String pass;
    @NonNull
    private String user;
    @NonNull
    private String image;
    @NonNull
    private List<String> containers;
    @NonNull
    private int startingPort;

    private Entrypoint n = new Entrypoint("", 8081);
    public String sendCommand(String command) {
        return new SSHCommandExecutor(ip, user, pass, port).executeCommand(command);
    }


    public void checkContainers() {
        if (containers.size() == 0)
            addContainer();
        for (int i = 0; i < containers.size(); i++) {

            if (Double.parseDouble(getCPU(containers.get(i)).replace("%", "")) > MAX_USAGE) {
                addContainer();
                restartReverseProxy();
            }
        }
    }

    public void addContainer() {
        if (containers.size() > MAX_CONTAINERS)
            return;
        UUID uniqueID = UUID.randomUUID();
        String uniqueIDString = uniqueID.toString();
        String output = sendCommand("echo " + getPass() + " | sudo -S docker run --name " + uniqueIDString + " -d -p " + startingPort + ":80" + " " + image);
        System.out.println(output);
        startingPort++;
        containers.add(uniqueIDString);

    }


    public String getCPU(String name) {
        return sendCommand("echo " + getPass() + " | sudo -S docker stats --no-stream " + name + " | awk 'NR > 1 {print $3}'");
    }

    //if cpu goes above limit add new endpoiint and restart reverseproxy
    public void restartReverseProxy() {
        n.stopServer();
        List<Endpoint> endpointList = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            endpointList.add(new Endpoint(ip, startingPort - i-1, pass, user));
        }



        n.startServer(endpointList, "");
    }
    public void startReverseProxy(){
        List<Endpoint> endpointList = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            endpointList.add(new Endpoint(ip, startingPort - i-1, pass, user));
        }
        n.startServer(endpointList, "");
    }

}
