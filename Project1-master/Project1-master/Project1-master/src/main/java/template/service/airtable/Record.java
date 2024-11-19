package template.service.airtable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
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
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        int pageSize = 100;
        String offset = null;
        boolean hasMoreRecords = true;
        JsonArray allRecords = new JsonArray();

        try
        {
            while (hasMoreRecords) {
                HttpGet request = new HttpGet(offset == null ? endpoint : endpoint + "&offset=" + offset);
                request.setHeader("Authorization", "Bearer " + personal_access_token);

                HttpResponse response = client.execute(request);

                HttpEntity entity = response.getEntity();
                String responseString = entity != null ? EntityUtils.toString(entity) : null;

                JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                if (jsonResponse.has("records"))
                {
                    JsonArray records = jsonResponse.get("records").getAsJsonArray();

                    for (int index = 0; index < records.size(); index++)
                    {
                        allRecords.add(records.get(index).getAsJsonObject());
                    }
                }

                if (jsonResponse.has("offset"))
                {
                    offset = jsonResponse.get("offset").getAsString();
                }
                else
                {
                    hasMoreRecords = false;
                }

            }

            JsonObject standardList = new JsonObject();
            standardList.add("records", allRecords);
            return standardList.toString();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    protected static String updateRecord(JsonObject fields, String recordId, String tableId, String baseId, String personal_access_token) {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId + "/" + recordId;
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        try
        {
            HttpPatch httpPatch = new HttpPatch(endpoint);
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPatch.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            JsonObject body = new JsonObject();
            body.add("fields", fields);
            httpPatch.setEntity(new StringEntity(body.toString()));

            HttpResponse response = client.execute(httpPatch);

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
            return null;
        }

    }

    protected static String createRecord(JsonObject fields, String tableId, String baseId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId;
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        try
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

            HttpResponse response = client.execute(httpPost);

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
            return null;
        }
    }

    protected static boolean dropRecord(String recordId, String tableId, String baseId, String personal_access_token)
    {
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableId + "/" + recordId;
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        try
        {
            HttpDelete httpDelete = new HttpDelete(endpoint);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            HttpResponse response = client.execute(httpDelete);

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
            return false;
        }
    }


}
