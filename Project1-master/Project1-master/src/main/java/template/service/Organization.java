package template.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Organization {
    public static final JsonArray tableSetting = JsonParser.parseString(" [\n" +
            "    {\n" +
            "      \"name\": \"id\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"businessPhones\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"displayName\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"givenName\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"jobTitle\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"mail\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"mobilePhone\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"officeLocation\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"preferredLanguage\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"surname\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"userPrincipalName\",\n" +
            "      \"type\": \"singleLineText\"\n" +
            "    }\n" +
            "  ]\n").getAsJsonArray();
    public static List<JsonObject> listUsersAsJson(String token)
    {
        try {
            String url = "https://graph.microsoft.com/v1.0/users";

            // Create an instance of HttpClient
            HttpClient httpClient = HttpClientBuilder.create().build();

            // Create an instance of HttpGet
            HttpGet httpGet = new HttpGet(url);

            // Set the authorization header
            httpGet.setHeader("Authorization", "Bearer " + token);

            // Execute the HTTP request
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot list users");
                return null;
            }

            // Get the response body as a String
            String body = EntityUtils.toString(response.getEntity());
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
                if (jsonObject.has("businessPhones"))
                {
                    // Get the value of "roles" as a comma-separated String
                    String rolesString = jsonObject.get("businessPhones").getAsJsonArray().toString();

                    // Update the JsonObject with the new value of "roles"
                    jsonObject.addProperty("businessPhones", rolesString);
                }
                result.add(jsonObject);

            }

            return result;

        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
