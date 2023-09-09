package loadbalancer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class ReverseProxyHandler implements HttpHandler {
    private Loadbalancer loadbalancer;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String targetResponse = loadbalancer.handleRequest(exchange);
        // Get the response output stream
        OutputStream os = exchange.getResponseBody();
        String response = "Received";

        exchange.sendResponseHeaders(200, 0);
        //System.out.println("Target response: "+targetResponse);
        os.write(targetResponse.getBytes());
        os.close();
    }
}

