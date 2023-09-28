package loadbalancer;

import ServerUtil.Endpoint;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Loadbalancer {
    private List<Endpoint> endpointList = new ArrayList<>();
    private int currentServerIndex;
    private String endpoint;
    public Endpoint getCurrentServer(){
        if(currentServerIndex++ >= endpointList.size())
            currentServerIndex = 0;
        return endpointList.get(currentServerIndex);
    }
    public List<Endpoint> getList(){
        return endpointList;
    }
    public String handleRequest(HttpExchange exchange){
       return getCurrentServer().forwardRequest(exchange, endpoint);
    }

}
