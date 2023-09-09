package ServerUtil;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Service {
    private String hostIP;
    private int port;

    public String forwardRequest(HttpExchange exchange, String endpoint){
        try {
            return new Request(exchange).sendRequest("http://"+hostIP+":"+port+"/"+endpoint);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
