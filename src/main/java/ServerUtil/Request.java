package ServerUtil;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@AllArgsConstructor
public class Request {
    private HttpExchange exchange;

    public String sendRequest(String urlString) throws IOException {
        // System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(exchange.getRequestMethod());

        exchange.getRequestHeaders().forEach((key, values) -> {
            for (String value : values) {
                connection.setRequestProperty(key, value);
            }
        });
        connection.setDoOutput(true);


        // Get the request body from the exchange
        if(exchange.getRequestMethod().equals("POST")) {
            InputStream requestBody = exchange.getRequestBody();
            // Copy the request body to the output stream of the HttpURLConnection
            try (OutputStream out = connection.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = requestBody.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                requestBody.close();
            }
        }
        int responseCode = connection.getResponseCode();
        // System.out.println("Response Code: " + responseCode);

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
