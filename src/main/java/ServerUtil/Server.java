package ServerUtil;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Server {
    private String hostIP;
    private String username;
    private String pass;
    private int port;
    private String endpoint;

    public String forwardRequest(HttpExchange exchange){
        try {
            return new Request(exchange).sendRequest("http://"+hostIP+":"+port+"/"+endpoint);
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
    }
}
