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
    private final int STARTING_PORT = 9000;
    private final int MAX_CONTAINERS = 50;
    private final int MAX_USAGE = 5;

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

    @NonNull
    private Entrypoint n;

    public List<String> containers() {
        return containers;
    }

    public String sendCommand(String command) {
        return new SSHCommandExecutor(ip, user, pass, port).executeCommand(command);
    }


    public void checkContainers() {
        System.out.println("I am " + ip);
        if (containers.size() == 0)
            addContainer();

        for (int i = 0; i < containers.size(); i++) {
            int times = 0;
            for (int j = 0; j < 3; j++) {
                if (Math.floor(Double.parseDouble(getCPU(containers.get(i)).replace("%", ""))) <= 0 && containers.size() > 1) {
                    times++;
                }
            }
            if (Math.ceil(Double.parseDouble(getCPU(containers.get(i)).replace("%", ""))) >= MAX_USAGE) {
                addContainer();
                break;
                //restartReverseProxy();
            }
            if (times == 3) {
                removeContainer(containers.get(i));
                containers.remove(i);
                i--;
            }
        }
    }

    public void removeContainer(String containerName) {
        System.out.println("Removed unused container " + containerName);
        sendCommand("echo " + getPass() + " | sudo -S docker kill " + containerName);
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
            endpointList.add(new Endpoint(ip, startingPort - i - 1, pass, user));
        }
        n.startServer(endpointList, "");
    }

    public void startReverseProxy() {
        // addExitHook();
        List<Endpoint> endpointList = new ArrayList<>();
        for (int i = 0; i < containers.size(); i++) {
            endpointList.add(new Endpoint(ip, startingPort - i - 1, pass, user));
        }
        //n.stopServer();
        n.startServer(endpointList, "");
    }

}
