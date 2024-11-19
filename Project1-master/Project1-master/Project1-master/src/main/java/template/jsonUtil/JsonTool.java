package template.jsonUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.*;

public class JsonTool {
    public static JsonObject getAccessInfo(String filepath)
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
            System.out.println("File not found");
            return null;
        }
    }


}
