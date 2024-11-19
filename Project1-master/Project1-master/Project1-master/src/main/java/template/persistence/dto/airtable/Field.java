package template.persistence.dto.airtable;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

public class Field {
    private final String id;
    private final String name;
    private final String type;

    protected Field(JsonObject field)
    {
        this.id = field.get("id").getAsString();
        this.name = field.get("name").getAsString();
        this.type = field.get("type").getAsString();
    }

    protected String getId()
    {
        return this.id;
    }

    protected String getName()
    {
        return this.name;
    }

    protected String getType()
    {
        return this.type;
    }

    protected static String createField(JsonObject field, String baseId, String tableId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/meta/bases/" + baseId + "/tables/" + tableId + "/fields";
        
        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {        
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            httpPost.setEntity(new StringEntity(field.toString()));

            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("Created field successfully");
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                return responseString;
            }
            else{
                System.out.println("Error code: " + response.getStatusLine().getStatusCode());
                return response.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }


    }

    protected static String updateField(JsonObject field, String fieldId, String baseId, String tableId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/meta/bases/" + baseId + "/tables/" + tableId + "/fields/" + "fieldId";

        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {        
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            httpPost.setEntity(new StringEntity(field.toString()));

            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("Updated field successfully");
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                return responseString;
            }
            else{
                System.out.println("Error code: " + response.getStatusLine().getStatusCode());
                return response.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
