package loadbalancer;

import ServerUtil.Service;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Loadbalancer {
    private List<Service> serviceList = new ArrayList<>();
    private int currentServerIndex;
    private String endpoint;
    public Service getCurrentServer(){
        currentServerIndex++;
        if(currentServerIndex > serviceList.size())
            currentServerIndex = 0;
        return serviceList.get(currentServerIndex);
    }
    public String handleRequest(HttpExchange exchange){
       return getCurrentServer().forwardRequest(exchange, endpoint);
    }

}
