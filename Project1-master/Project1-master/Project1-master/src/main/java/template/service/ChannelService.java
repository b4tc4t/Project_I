package template.service;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import template.colorUtil.Color;
import template.team_config.Config;


import java.io.IOException;
import java.net.URISyntaxException;


public class ChannelService {
    public static void createChannel(String teamId, String displayName, String description) throws IOException, InterruptedException {
        String membershipType = "standard";
        String url = "https://graph.microsoft.com/v1.0/teams/" + teamId + "/channels";
        String body = String.format("{\n" +
                "  \"displayName\": \"%s\",\n" +
                "  \"description\": \"%s\",\n" +
                "  \"membershipType\": \"%s\"\n" +
                "}", displayName, description, membershipType);

        try {
            // Create a new HttpClient object
            HttpClient httpClient = HttpClientBuilder.create().build();

            // Create a new HttpPost object
            HttpPost httpPost = new HttpPost(url);

            // Set request headers
            httpPost.setHeader("Content-type", "application/json");

            // Set the Authorization header with the access token
            String accessToken = Config.getAccessToken();
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            // Set request body
            StringEntity params = new StringEntity(body);
            httpPost.setEntity(params);

            // Send request
            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 201) {
                Color.printBlue("Created channel " + displayName + " success");
            } else {
                if (response.getStatusLine().getStatusCode() == 404) {
                    Color.printYellow("Group not found");
                } else  {
                    Color.printYellow("Failed to create channel (Maybe because of wrong groupID/display name/description format or channel existed ");

                }
                //System.out.printf("Failed to create channel. Status code: %d, Error message: %s", response.getStatusLine().getStatusCode(), response.getEntity().getContent().toString());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static String listAllChannels(String groupId) throws InterruptedException
    {
        String endpoint = "https://graph.microsoft.com/v1.0/teams/" + groupId + "/channels";
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try {
            HttpGet httpGet = new HttpGet(endpoint);

            String token = Config.getAccessToken();
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        } catch (IOException e) {
            System.out.println("Oops! We ran into some problems");
            return null;
        }
    }

}


