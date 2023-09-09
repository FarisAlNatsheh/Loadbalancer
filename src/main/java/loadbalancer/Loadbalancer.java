package loadbalancer;

import ServerUtil.Server;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Loadbalancer {
    private List<Server> serverList = new ArrayList<>();
    private int currentServerIndex;

    public Server getCurrentServer(){
        currentServerIndex++;
        if(currentServerIndex >= serverList.size())
            currentServerIndex = 0;
        return serverList.get(currentServerIndex);
    }
    public String handleRequest(HttpExchange exchange){
       return serverList.get(currentServerIndex).forwardRequest(exchange);
    }

}
