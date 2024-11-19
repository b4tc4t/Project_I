package template.persistence.dto.airtable;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

public class Record {
    
    private String id;
    private JsonObject fields;
    private String idField;

    protected String getId()
    {
        return this.id;
    }

    protected String getIdField()
    {
        return this.idField;
    }

    protected JsonObject getFields()
    {
        return this.fields;
    }

    Record(JsonObject record)
    {
        this.id = record.get("id").getAsString();
        this.fields = record.get("fields").getAsJsonObject();
        if (this.fields.has("id"))
            this.idField = this.fields.get("id").getAsString();
        else 
            this.idField = null;
    }

    protected boolean equals(JsonObject fields, List<Field> fieldsList)
    {
        for (Field field:fieldsList)
        {
            if (fields.has(field.getName()))
            {
                String newValue = fields.get(field.getName()).toString();
                if (newValue.equals("null") || newValue.equals("[]") || newValue.equals("false"))
                    continue;
                if (!this.fields.has(field.getName()))
                {
                    return false;
                }
                String oldValue = this.fields.get(field.getName()).toString();
                if (field.getType().contains("date"))
                    if (oldValue.replaceAll(".000Z", "Z").equals(newValue))
                        continue;
                if (!newValue.equals(oldValue)) 
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    protected static String listRecords(String tableId, String baseId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId;

        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpGet httpGet = new HttpGet(endpoint);
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);
            
            CloseableHttpResponse response = client.execute(httpGet);
            
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected static String updateRecord(JsonObject fields, String recordId, String tableId, String baseId, String personal_access_token) {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId + "/" + recordId;
        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpPatch httpPatch = new HttpPatch(endpoint);
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPatch.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            JsonObject body = new JsonObject();
            body.add("fields", fields);
            httpPatch.setEntity(new StringEntity(body.toString()));

            CloseableHttpResponse response = client.execute(httpPatch);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                return responseString;
            }
            else{
                return null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    protected static String createRecord(JsonObject fields, String tableId, String baseId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId;

        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            JsonObject body = new JsonObject();
            body.add("fields", fields);

            JsonArray records = new JsonArray();
            records.add(body);

            JsonObject fullBody = new JsonObject();
            fullBody.add("records", records);


            httpPost.setEntity(new StringEntity(fullBody.toString()));

            CloseableHttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                return responseString;
            }
            else{
                System.out.println("Error: Create record. Status code " + response.getStatusLine().getStatusCode());
                System.out.println("fullbody: " + fullBody);
                return null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected static boolean dropRecord(String recordId, String tableId, String baseId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId + "/" + recordId;

        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpDelete httpDelete = new HttpDelete(endpoint);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            CloseableHttpResponse response = client.execute(httpDelete);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("Record " + recordId + "has been deleted");
                return true;
            }
            else{
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }


}
