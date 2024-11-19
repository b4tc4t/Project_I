package template.team_config;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONObject;
import template.service.JsonTool;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.*;


public class config {
    private static final Logger logger = Logger.getLogger(config.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/users";
        String token = getAccessToken(); // Replace with your actual access token

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(graphEndpoint))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();
        JSONObject json = new JSONObject(response.body());
        System.out.println(json);
        // Parse the JSON response using Gson
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(jsonResponse, Map.class);
        List<Map<String, Object>> valueList = (List<Map<String, Object>>) map.get("value");

        // Print the properties of each user
        /*
        int count = 1;
        for (Map<String, Object> valueMap : valueList) {
            System.out.println("User " + count);
            count += 1;
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                System.out.println(key + ": " + val);
            }
            System.out.println();
        }*/
    }


    public static void deleteAllUsers(String path) {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                String userPrincipalName = row[2];
                deleteUser(userPrincipalName);
            }
        } catch (IOException | CsvException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void deleteUser(String userPrincipalName) throws IOException, InterruptedException {
        String graphUrl = "https://graph.microsoft.com/v1.0/users/" + userPrincipalName;
        String accessToken = getAccessToken();

        URL url = new URL(graphUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = con.getResponseCode();
        if (responseCode >= 400) {
            logger.log(Level.INFO,"Failed to delete user: {0}." , userPrincipalName);
        } else {
            logger.log(Level.INFO,"User deleted: {0}." , userPrincipalName);
        }
    }

    public static String getAccessToken() throws IOException, InterruptedException {
        JsonObject accessJson = JsonTool.getAccessInfo("template/team_config/config.json");
        String TENANT_ID = accessJson.get("TENANT_ID").getAsString();
        String CLIENT_ID = accessJson.get("CLIENT_ID").getAsString();
        String CLIENT_SECRET = accessJson.get("CLIENT_SECRET").getAsString();


        String tokenEndpoint = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token";
        String encodedCredentials = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedCredentials)
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&scope=https://graph.microsoft.com/.default"))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new GsonBuilder().create();
        String accessToken = gson.fromJson(response.body(), Map.class).get("access_token").toString();

        return accessToken;
    }
}
