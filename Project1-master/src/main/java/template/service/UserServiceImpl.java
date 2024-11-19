package template.service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static template.api_config.config.getAccessToken;

public class UserServiceImpl implements UserService{
    private String token = getAccessToken();
    public UserServiceImpl() throws IOException, InterruptedException {
    }
    public void getUser() throws IOException, InterruptedException {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/users";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(graphEndpoint))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        System.out.println(response.body());
        Map<String, Object> map = gson.fromJson(response.body(), Map.class);

        List<Map<String, Object>> valueList = (List<Map<String, Object>>) map.get("value");
        int count = 1;
        for (Map<String, Object> valueMap : valueList) {
            System.out.println(count);
            count +=1;
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                System.out.println(key + ": " + val);
            }
        }
    }


    public String getUserAsJSON() throws IOException, InterruptedException
    {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/users";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(graphEndpoint))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    
    public void createUser(String displayName ,String mailNickname ,String userPrincipalName ,String password) throws IOException, InterruptedException{
        // Step 1: Get an access token
        String accessToken = getAccessToken();
        // Step 2: Make an HTTP request to the Microsoft Graph API
        // Step 2: Create a new user
        String userJson = "{\r\n" +
                "  \"accountEnabled\": true,\r\n" +
                "  \"displayName\": \"" + displayName + "\",\r\n" +
                "  \"mailNickname\": \"" + mailNickname + "\",\r\n" +
                "  \"userPrincipalName\": \"" + userPrincipalName + "\",\r\n" +
                "  \"passwordProfile\": {\r\n" +
                "    \"forceChangePasswordNextSignIn\": true,\r\n" +
                "    \"password\": \"" + password + "\"\r\n" +
                "  }\r\n" +
                "}";
        URL url = new URL("https://graph.microsoft.com/v1.0/users");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        // Step 3: Print the response to the console
        int responseCode = con.getResponseCode();
        Gson gson = new GsonBuilder().create();
        BufferedReader in;
        if (responseCode >= 400) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                errorResponse.append(line);
            }
            // Parse the JSON string
            JsonObject jsonObject = gson.fromJson(errorResponse.toString(), JsonObject.class);
            // Get the value of the "message" property
            String message = jsonObject.getAsJsonObject("error").get("message").getAsString();
            // Print the error message
            System.out.println("Error: " + message);
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("Create user success");
            System.out.println(in.readLine());
        }

        in.close();
    }


}
