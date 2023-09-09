package ServerUtil;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@AllArgsConstructor
public class Request {
    private HttpExchange exchange;
    public String sendRequest(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println(1);
        connection.setRequestMethod(exchange.getRequestMethod());
        exchange.getRequestHeaders().forEach((key, values) -> {
            for (String value : values) {
                connection.setRequestProperty(key, value);
            }
        });

        int responseCode = connection.getResponseCode();

        System.out.println("Response Code: " + responseCode);

        StringBuilder response = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        connection.disconnect();

        return response.toString();
    }
}
