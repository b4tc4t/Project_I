package template.service;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import template.api_config.config;

public class GroupService {

    //return group id if created successfully
    public static String create(String description, String displayName, 
                                String mailNickname
                                ) throws IOException, InterruptedException
    {
        String OBJECT_ID = config.getObjectID();
        String accessToken = config.getAccessToken();


        // Set the request body
        String requestBody = String.format(
                            "{\"description\": \"%s\","
                            + "\"displayName\": \"%s\","
                            + "\"groupTypes\": [\"Unified\"],"
                            + "\"mailEnabled\": false,"
                            + "\"mailNickname\": \"%s\","
                            + "\"securityEnabled\": true,"
                            + "\"owners@odata.bind\": [\"https://graph.microsoft.com/v1.0/users/"+OBJECT_ID+"\"],"
                            + "\"members@odata.bind\": [\"https://graph.microsoft.com/v1.0/users/3eb3c149-d420-4fb3-a00a-f46217a15920\"]"
                            + "}",
                            description, displayName, mailNickname);


        // Set the request headers
        HttpPost request = new HttpPost("https://graph.microsoft.com/v1.0/groups");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setEntity(new StringEntity(requestBody));

        // Send the request and get the response
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);

        // Check the response status code
        String group_id = "";
        if (response.getStatusLine().getStatusCode() == 201) {
            System.out.println("Group created successfully!");
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));

            group_id = jsonObject.getString("id");

        } else {
            System.out.printf("Failed to create group. Status code: %d, Error message: %s", response.getStatusLine().getStatusCode(), response.getEntity().getContent().toString());
        }

        // Close the response and the HttpClient
        response.close();
        httpClient.close();
        return group_id;
    }

    public static void listExistedIDsGroup() throws IOException, InterruptedException
    {
        String accessToken = config.getAccessToken();
        
        // Set the request headers
        HttpGet request = new HttpGet("https://graph.microsoft.com/v1.0/groups");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Send the request and get the response
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);

        // Check the response status code
        if (response.getStatusLine().getStatusCode() == 200) {
            // Get the response body as a string
            String responseBody = EntityUtils.toString(response.getEntity());

            // Parse the JSON response
            JSONObject responseJson = new JSONObject(responseBody);

            // Get the array of groups from the response
            JSONArray groupsJson = responseJson.getJSONArray("value");

            // Print the IDs of the groups
            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject groupJson = groupsJson.getJSONObject(i);
                String groupId = groupJson.getString("id");
                System.out.println(groupId);
            }
        } else {
            System.out.printf("Failed to list groups. Status code: %d, Error message: %s", response.getStatusLine().getStatusCode(), response.getEntity().getContent().toString());
        }

        // Close the response and the HttpClient
        response.close();
        httpClient.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        //String group_id = GroupService.create("example 1", "NobiPhus Group", "nobiphus");
        //System.out.println(group_id);

        String group_id = GroupService.create("Test 3", "NOBIPHUS Corp", "gnas");
        System.out.println(group_id);
    }

    
}
