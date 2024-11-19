package template.service;


import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import template.team_config.config;


import java.io.IOException;


public class ChannelService {
    public static void create(String teamId, String displayName, String description) throws IOException, InterruptedException
    {
        //String teamId = "7e8dcfe0-1fd3-4226-aeb0-63e1cc394c39";
        String membershipType = "standard";
        String url = "https://graph.microsoft.com/v1.0/teams/" + teamId + "/channels";
        String body = String.format("{\n" +
                "  \"displayName\": \"%s\",\n" +
                "  \"description\": \"%s\",\n" +
                "  \"membershipType\": \"%s\"\n" +
                "}", displayName, description, membershipType);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        // Set request headers
        request.setHeader("Content-type", "application/json");


        // Set the Authorization header with the access token
        String accessToken = config.getAccessToken();
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // Set request body
        StringEntity params = new StringEntity(body);
        request.setEntity(params);

        // Send request
        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 201) {
            System.out.println("Created channel " + displayName + " successfully!");
            /*
            // Read response
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            // Print response
            System.out.println("Response code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + result.toString());
            */
        }else{
            System.out.printf("Failed to create channel. Status code: %d, Error message: %s", response.getStatusLine().getStatusCode(), response.getEntity().getContent().toString());
        }
    }

}


