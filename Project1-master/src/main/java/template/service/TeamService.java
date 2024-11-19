package template.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



import template.api_config.config;

public class TeamService {

    public static void create(String group_id) throws IOException, InterruptedException
    {
        String accessToken = config.getAccessToken();


        // Set the team settings JSON payload
        String teamSettingsJson = "{\n" +
                "\"memberSettings\": {\n" +
                "\"allowCreatePrivateChannels\": true,\n" +
                "\"allowCreateUpdateChannels\": true\n" +
                "},\n" +
                "\"messagingSettings\": {\n" +
                "\"allowUserEditMessages\": true,\n" +
                "\"allowUserDeleteMessages\": true\n" +
                "},\n" +
                "\"funSettings\": {\n" +
                "\"allowGiphy\": true,\n" +
                "\"giphyContentRating\": \"strict\"\n" +
                "}\n" +
                "}";

        // Create an HTTP client
        HttpClient httpClient = HttpClientBuilder.create().build();

        // Create an HTTP PUT request
        HttpPut request = new HttpPut("https://graph.microsoft.com/v1.0/groups/" + group_id + "/team");

        // Set the Authorization header with the access token
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // Set the Content-Type header
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        try {
            // Set the request body
            StringEntity entity = new StringEntity(teamSettingsJson);
            request.setEntity(entity);

            // Execute the request
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 201) {
                System.out.println("Group created successfully!");
                // Get the response body
                //HttpEntity responseEntity = response.getEntity();
                //String responseBody = EntityUtils.toString(responseEntity);

                // Print the response body
                //System.out.println(responseBody);
            } else {
                System.out.printf("Failed to create group. Status code: %d, Error message: %s", response.getStatusLine().getStatusCode(), response.getEntity().getContent().toString());
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<JsonObject> listUsersAsJson(String group_id, String token) throws IOException, InterruptedException
    {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/teams/"+ group_id +"/members";
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try{
            HttpGet httpGet = new HttpGet(graphEndpoint);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot list users of Team has id: " +group_id);
                return null;
            }

            String body =  EntityUtils.toString(response.getEntity());
            JsonObject bodyJson = JsonParser.parseString(body).getAsJsonObject();

            JsonArray jsonArray = new JsonArray();

            if (bodyJson.has("value"))
            {
                jsonArray = bodyJson.get("value").getAsJsonArray();
            }

            List<JsonObject> result = new ArrayList<>();

            for(int i=0; i< jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                if (jsonObject.has("roles")) {
                    // Get the value of "roles" as a comma-separated String
                    String rolesString = jsonObject.get("roles").getAsJsonArray().toString();

                    // Update the JsonObject with the new value of "roles"
                    jsonObject.addProperty("roles", rolesString);

                    result.add(jsonObject);
                }

                if (jsonObject.has("businessPhones"))
                {
                    // Get the value of "roles" as a comma-separated String
                    String rolesString = jsonObject.get("businessPhones").getAsJsonArray().toString();

                    // Update the JsonObject with the new value of "roles"
                    jsonObject.addProperty("businessPhones", rolesString);
                }

            }

            return result;



        }catch(IOException e)
        {
            System.out.println("Cannot list users of Team has id: " +group_id);
            return null;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        String group_id = "7e8dcfe0-1fd3-4226-aeb0-63e1cc394c39";
        String token = config.getAccessToken();
        List<JsonObject> abc = TeamService.listUsersAsJson(group_id, token);
        for(JsonObject j : abc)
        {
            System.out.println(j.toString());
        }
    }

    
}
