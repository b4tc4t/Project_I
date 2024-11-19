package template.service.airtable;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import template.api_config.config;
import template.service.JsonTool;
import template.service.Organization;
import template.service.GroupService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table{
    private int i;
    private final String id;
    private final String name;
    private final List<Field> fields = new ArrayList<>();

    private final List<Record> records = new ArrayList<>();

    protected String getName() {
        return this.name;
    }
    protected String getId() {
        return this.id;
    }
    protected int getI() {
        return this.i;
    }
    protected int getNumRecords() {
        return records.size();
    }

    protected Field getField(String name)
    {
        for (Field field : this.fields)
        {
            if (field.getName().equals(name))
            {
                return field;
            }
        }
        return null;
    }
    protected Table(JsonObject table, String baseId, String personal_access_token)
    {
        this.id = table.get("id").getAsString();
        this.name = table.get("name").getAsString();
        table.get("fields").getAsJsonArray().forEach(field -> this.fields.add(new Field(field.getAsJsonObject())));

        syncRecord(baseId, personal_access_token);
    }

    protected void syncRecord(String baseId, String personal_access_token)
    {
        records.clear();
        String records = Record.listRecords(id, baseId, personal_access_token);
        if (records == null){
            System.out.println("Error: Cannot get records in table: " + name);
        }
        else
        {
            JsonObject recordsJson = new Gson().fromJson(records, JsonObject.class);
            JsonArray listRecords = recordsJson.get("records").getAsJsonArray();
            listRecords.forEach(record -> this.records.add(new Record(record.getAsJsonObject())));
        }
    }

    protected boolean addField(JsonObject field, String baseId, String token) {
        String fieldCreate = Field.createField(field, id, baseId, token);
        if (fieldCreate == null) {
            System.out.println("Error: Cannot create field: " + field.get("name").getAsString() + " in table: " + name);
            return false;
        }
        JsonObject fieldJson = JsonParser.parseString(fieldCreate).getAsJsonObject();
        fields.add(new Field(fieldJson));
        System.out.println("Created field: " + field.get("name").getAsString() + " in table: " + name);
        return true;
    }

    private boolean updateRecord(JsonObject fields, Record record, String baseId, String personal_access_token)
    {
        String recordUpdate = Record.updateRecord(fields, record.getId(), id, baseId, personal_access_token);
        if (recordUpdate == null)
        {
            return false;
        }
        JsonObject recordJson = JsonParser.parseString(recordUpdate).getAsJsonObject();
        records.remove(record);
        records.add(new Record(recordJson));
        //print
        return true;
    }

    private boolean addRecord(JsonObject fields, String baseId, String personal_access_token)
    {
        String recordCreate = Record.createRecord(fields, id, baseId, personal_access_token);
        if (recordCreate == null)
        {
            //print
            return false;
        }
        JsonObject recordJson = new Gson().fromJson(recordCreate, JsonObject.class);
        JsonArray listRecords = recordJson.get("records").getAsJsonArray();

        //records.clear();
        listRecords.forEach(record -> this.records.add(new Record(record.getAsJsonObject())));

        //print
        return true;
    }

    protected Record getRecord(String idField)
    {
        for (Record record : this.records)
        {
            if (record.getIdField().equals(idField))
            {
                return record;
            }
        }
        return null;
    }

    private boolean pullRecord(JsonObject fields, String baseId, String token)
    {
        Record oldRec = getRecord(fields.get("id").getAsString());
        if (oldRec == null)
        {
            if (addRecord(fields, baseId, token))
            {
                //print("Add record to table name"
                return true;
            }
            return false;
        }
        if (oldRec.equals(fields, this.fields))
        {
            return true;
        }

        if (updateRecord(fields, oldRec, baseId, token))
        {
            //print Update record: record ID in table
            return true;
        }
        return false;
    }

    protected boolean pullAllRecords(List<JsonObject> fields, String baseId, String personal_access_token)
    {
        for (JsonObject field : fields)
        {
            if (!pullRecord(field, baseId, personal_access_token))
            {
                //print Cannot pull record: user ID in table name
                return false;
            }
        }
        //print Pulled all records in table name
        System.out.println("Pulled all records in the table: " + this.name);
        return true;
    }

    private void dropRecord(List<JsonObject> fields, String baseId, String personal_access_token)
    {
        List<Record> dropList = new ArrayList<>();
        for (Record record : this.records)
        {
            boolean isExist = false;
            for (JsonObject field : fields)
            {
                if (record.getIdField().equals(field.get("id").getAsString()))
                {
                    isExist = true;
                    break;
                }
            }
            if (!isExist)
            {
                if (Record.dropRecord(record.getId(), id, baseId, personal_access_token))
                {
                    //print Deleted record: record.getIdField() in table name
                    dropList.add(record);
                }
                else
                {
                    //print cannot delete record: record.getIdField
                }
            }
        }
        this.records.removeAll(dropList);
    }

    protected static String listTables(String baseId, String personal_access_token) {
        String endpoint = "https://api.airtable.com/v0/meta/bases/" + baseId + "/tables";
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try {
            HttpGet httpGet = new HttpGet(endpoint);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Error: Could not list tables");
                return null;
            }
            System.out.println("Listed tables");
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        } catch (IOException e) {
            System.out.println("Error: Could not list tables. Exception: " + e.getMessage());
            return null;
        }
    }

    protected static String createTable(String name, JsonArray fields, String baseId, String personal_access_token){
        String endpoint = "https://api.airtable.com/v0/meta/bases/" + baseId + "/tables";

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        try  {

            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + personal_access_token);

            JsonObject body = new JsonObject();
            body.addProperty("name", name);
            body.add("fields", fields);

            httpPost.setEntity(new StringEntity(body.toString()));

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Error: Could not create table: " + name);
                return null;
            }
            System.out.println("Created table: " + name);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("Error: Could not create table: " + name + ". Message: " + e.getMessage());
            return null;
        }
    }
    protected static JsonObject getTable(String tableId, String baseId, String personal_access_token)
    {
        JsonObject tables = JsonParser.parseString(listTables(baseId, personal_access_token)).getAsJsonObject();
        JsonArray tablesArray = tables.get("tables").getAsJsonArray();
        for(int i=0; i< tablesArray.size(); i++)
        {
            JsonObject a = tablesArray.get(i).getAsJsonObject();
            if (a.get("id").getAsString().equals(tableId))
            {
                return a;
            }
        }
        return null;
    }
    protected void writeTableToXLSX(String filename, String baseId, String token)
    {
        this.syncRecord(baseId, token);
        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(name);

            // Create the header row
            Row headerRow = sheet.createRow(0);
            int columnCount = 0;
            for (Field field : fields) {
                Cell cell = headerRow.createCell(columnCount++);
                cell.setCellValue(field.getName());
            }

            List<String> headers = new ArrayList<>();
            for (Field field : fields) {
                headers.add(field.getName());
            }

            // Create the data rows
            int rowCount = 1;
            for (Record record : records) {
                JsonObject fields = record.getFields();

                Row row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(columnCount++);
                    if (fields.has(header)) {
                        JsonElement field = fields.get(header);
                        cell.setCellValue(field.toString());
                    }
                }
            }

            // Write the workbook to a file
            FileOutputStream outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            System.out.println("Wrote table: " + name + " to file: " + filename);
        }
        catch(IOException e)
        {
            System.out.print("Cannot write table");
        }
    }


}
