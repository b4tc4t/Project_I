package template.team_config;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONObject;
import template.jsonUtil.JsonTool;

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


public class Config {
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    //Change the value of variable Config to absolute path of Config.json
    private final static String configAirTable = "C:\\Users\\LamPhuss\\Downloads\\Project_I-main (1)\\Project_I-main\\Project1-master\\Project1-master\\Project1-master\\src\\main\\java\\template\\accessInfo\\configAirTable.json";
    private final static String configAzure = "C:\\Users\\LamPhuss\\Downloads\\Project_I-main (1)\\Project_I-main\\Project1-master\\Project1-master\\Project1-master\\src\\main\\java\\template\\accessInfo\\configAzure.json";

    public static String getConfigAirTable()
    {
        return configAirTable;
    }

    public static String getConfigAzure()
    {
        return configAzure;
    }

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


    }
    public static String getAccessToken() throws IOException, InterruptedException {
        JsonObject accessJson = JsonTool.getAccessInfo(configAzure);
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
