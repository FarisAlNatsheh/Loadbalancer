package ServerUtil;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Endpoint {
    private String hostIP;
    private int port;
    private String password;
    private String username;

    public String forwardRequest(HttpExchange exchange, String endpoint){
        try {
            return new Request(exchange).sendRequest("http://"+hostIP+":"+port+"/"+endpoint);
        } catch (IOException e) {
//            System.out.println("http://"+hostIP+":"+port+"/"+endpoint);
//            System.out.println(e);
            return e.getMessage();
        }
    }
}
