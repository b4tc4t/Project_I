import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
//import org.json.JSONMLParserConfiguration;
import org.json.JSONObject;

public class AirtableExample {
    public static void main(String[] args) throws IOException {
        String personal_access_token = "patjdKcKidNingLll.66ac7db478d17b15a22aed3cd0b01b333ef00b37cea45b31b904a1b06d687732";
        String baseId = "appbhunVaQ7iaLBoF";
        String tableName = "tbl6EI2AUbMfTOL2p";
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + personal_access_token);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString()); // Raw JSON response from the API
        JSONObject jsonObject = new JSONObject(response.toString());
        JSONArray records = jsonObject.getJSONArray("records");
        for (int i=0; i < records.length(); i++)
        {
            JSONObject record = records.getJSONObject(i);
            String recordID = record.getString("id");
            System.out.println(recordID);
        }

    }
}