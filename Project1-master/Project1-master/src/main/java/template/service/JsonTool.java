package template.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.*;

public class JsonTool {
    public static JsonArray getFieldFromFile(String filepath)
    {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonElement jsonElement = jsonParser.parse(new FileReader(filepath));
            JsonArray fields = new JsonArray();
            if (jsonElement.isJsonArray()) {

                // cast the JsonElement to a JsonArray object
                //System.out.println("ABC");
                fields = jsonElement.getAsJsonArray();
            }
            return fields;
        }
        catch(FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static JsonObject getAccessInfoAirTable(String filepath)
    {
        JsonParser jsonParser = new JsonParser();
        try{
            JsonElement jsonElement1 = jsonParser.parse(new FileReader(filepath));
            JsonObject access = new JsonObject();
            if (jsonElement1.isJsonObject()) {

                // cast the JsonElement to a JsonArray object
                access = jsonElement1.getAsJsonObject();

            }
            return access;
        }
        catch(FileNotFoundException e)
        {
            throw  new RuntimeException(e);
        }
    }
    public static String[] convertJsonArrayToStringArray(JsonArray businessPhones)
    {
        // Convert JsonArray to String array
        String[] stringArray = new String[businessPhones.size()];
        for (int i = 0; i < businessPhones.size(); i++) {
            JsonElement abc = businessPhones.get(i);
            stringArray[i] = abc.getAsString();
        }
        return stringArray;
    }

    public static String formatBusinessPhones(String json)
    {
        // Parse the JSON string into a JsonObject
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        // Get the array of users from the parsed JSON object
        JsonArray users = jsonObject.getAsJsonArray("value");

        // Loop through the users
        for (JsonElement user : users) {
            // Get the businessPhones array for the current user
            JsonArray businessPhones = user.getAsJsonObject().getAsJsonArray("businessPhones");
            
            String[] converttedBusinessPhones = JsonTool.convertJsonArrayToStringArray(businessPhones);

            // Format the businessPhones array as a string
            String formattedBusinessPhones = String.join("\n", converttedBusinessPhones);

            // Set the formatted businessPhones string in the current user's JsonObject
            user.getAsJsonObject().addProperty("businessPhones", formattedBusinessPhones);
        }

        // Convert the JsonObject back to a JSON string
        String formattedJson = new Gson().toJson(jsonObject);
        return formattedJson;
    }

}
