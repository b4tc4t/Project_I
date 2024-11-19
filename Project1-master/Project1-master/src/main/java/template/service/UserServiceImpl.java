package template.service;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONObject;
import template.persistence.dto.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static template.api_config.config.getAccessToken;

public class UserServiceImpl implements UserService {
    private String token = getAccessToken();
    private static final Logger logger = Logger.getLogger("MyLog");

    public UserServiceImpl() throws IOException, InterruptedException {
        // TODO document why this constructor is empty
    }



    public void getUser() throws IOException, InterruptedException {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/users";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(graphEndpoint))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(response.body(), Map.class);
        List<Map<String, Object>> valueList = (List<Map<String, Object>>) map.get("value");
        int count = 1;
        for (Map<String, Object> valueMap : valueList) {
            System.out.println(count);
            count += 1;
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                System.out.println(key + ": " + val);
            }
        }
    }

    public void getUserByPrincipalName(String principalName){
        String graphEndpoint = String.format("https://graph.microsoft.com/v1.0/users/%s", principalName);
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(graphEndpoint).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = reader.readLine();
                reader.close();
                JSONObject jsonObject = new JSONObject(response);

                StringBuilder sb = new StringBuilder();
                for (String key : jsonObject.keySet()) {
                    Object value = jsonObject.get(key);
                    sb.append(key).append(": ").append(value).append("\n");
                }

                String resultString = sb.toString();
                System.out.println(resultString);
            } else if (con.getResponseCode() == 404) {
                System.out.println("User not found");
            } else {
                System.out.println("Request failed with response code: " + con.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(User newUser) throws IOException, InterruptedException {
        // Step 1: Get an access token
        String accessToken = getAccessToken();
        // Step 2: Make an HTTP request to the Microsoft Graph API
        // Step 2: Create a new user
        String userJson = "{\r\n" +
                "  \"accountEnabled\": true,\r\n" +
                "  \"displayName\": \"" + newUser.getDisplayName() + "\",\r\n" +
                "  \"mailNickname\": \"" + newUser.getMailNickName() + "\",\r\n" +
                "  \"userPrincipalName\": \"" + newUser.getUserPrincipalName() + "\",\r\n" +
                "  \"passwordProfile\": {\r\n" +
                "    \"forceChangePasswordNextSignIn\": true,\r\n" +
                "    \"password\": \"" + newUser.getPassword() + "\"\r\n" +
                "  }\r\n" +
                "}";
        URL url = new URL("https://graph.microsoft.com/v1.0/users");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        // Step 3: Print the response to the console
        int responseCode = con.getResponseCode();
        Gson gson = new GsonBuilder().create();
        BufferedReader in;
        if (responseCode >= 400) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                errorResponse.append(line);
            }
            // Parse the JSON string
            JsonObject jsonObject = gson.fromJson(errorResponse.toString(), JsonObject.class);
            // Get the value of the "message" property
            String message = jsonObject.getAsJsonObject("error").get("message").getAsString();
            // Print the error message
            System.out.println("Error: " + message);
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("Create user success");
            System.out.println(in.readLine());
        }

        in.close();
    }

    public void deleteUser(String id) throws IOException, InterruptedException {
        // Set the API endpoint and user ID
        String endpoint = "https://graph.microsoft.com/v1.0/users/{user-id}";

        // Create a URL object and open a connection
        URL url = new URL(endpoint.replace("{user-id}", id));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Set the request method to DELETE
        con.setRequestMethod("DELETE");

        // Set the authorization header
        con.setRequestProperty("Authorization", "Bearer " + token);

        // Make the request and check for errors
        int responseCode = con.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            // User deleted successfully
            System.out.println("User deleted successfully.");
        } else {
            // Handle the error response
            System.out.println("Error deleting user: " + con.getResponseMessage());
        }

    }

    public static void createMultipleUsersInOneRequest(List<JsonObject> listUser) throws IOException, InterruptedException {
        logger.setLevel(Level.INFO);
        logger.info("Creating multiple users in one request...");
        String accessToken = getAccessToken();
        JsonArray jsonArray = new JsonArray();
        for (JsonObject user : listUser) {
            JsonObject jsonObject = new JsonObject();
            // id
            jsonObject.addProperty("id", String.valueOf(listUser.indexOf(user)));
            // method
            jsonObject.addProperty("method", "POST");
            // url
            jsonObject.addProperty("url", "/users");
            // headers
            JsonObject headers = new JsonObject();
            headers.addProperty("Content-Type", "application/json");
            jsonObject.add("headers", headers);
            // body
            jsonObject.add("body", user);
            jsonArray.add(jsonObject);
        }

        JsonObject batchRequest = new JsonObject();
        batchRequest.add("requests", jsonArray);

        //POST https://graph.microsoft.com/v1.0/$batch
        //Accept: application/json
        //Content-Type: application/json

        URL url = new URL("https://graph.microsoft.com/v1.0/$batch");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(batchRequest.toString());
        wr.flush();
        wr.close();

        // Step 3: Print the response to the console

        int responseCode = con.getResponseCode();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        BufferedReader in;
        if (responseCode >= 400) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                errorResponse.append(line);
            }
            // Parse the JSON string
            JsonObject jsonObject = gson.fromJson(errorResponse.toString(), JsonObject.class);
            // Get the value of the "message" property
            String message = jsonObject.getAsJsonObject("error").get("message").getAsString();
            // Print the error message
            logger.warning("Error: " + message);
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            logger.info("Batch request fetch success :");
            JsonObject responseObj = gson.fromJson(in, JsonObject.class);
// get the array of responses from the JsonObject
            JsonArray responses = responseObj.getAsJsonArray("responses");
            for (JsonElement resp : responses) {
                JsonObject respObj = resp.getAsJsonObject();
                String respId = respObj.get("id").getAsString();
                int respIdInt = Integer.parseInt(respId);
                int respStatus = respObj.get("status").getAsInt();
                if (respStatus >= 400) {
                    String message = respObj.getAsJsonObject("body")
                            .getAsJsonObject("error")
                            .get("message").getAsString();
                    logger.warning("User Principal Name : " + listUser.get(respIdInt).get("userPrincipalName") + "\n" +
                            "Response Status : " + respStatus + "\n" +
                            "Error : " + message);
                    //logger.info("==========================================================================");
                } else {
                    logger.info("User Principal Name : " + listUser.get(respIdInt).get("userPrincipalName") + "\n" +
                            "Response Status : " + respStatus + "\n" +
                            "Create user success ");
                    //logger.info("==========================================================================");
                }
            }
        }

        in.close();

    }

    public void createAllUsers(String path) {
        List<JsonObject> listAllUser = new ArrayList<>();
        try {
            FileHandler fh = new FileHandler("batch_request.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.warning("Error creating log file: " + e.getMessage());
        }
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();


            // Process each row
            for (String[] row : rows) {
                JsonObject user = new JsonObject();

                // Set the user's properties
                user.addProperty("accountEnabled", true);
                user.addProperty("displayName", row[0]);
                user.addProperty("mailNickname", row[1]);
                user.addProperty("userPrincipalName", row[2]);
                JsonObject passwordProfile = new JsonObject();
                passwordProfile.addProperty("forceChangePasswordNextSignIn", true);
                passwordProfile.addProperty("password", row[3]);
                user.add("passwordProfile", passwordProfile);

                // Add the user to the array
                listAllUser.add(user);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        List<JsonObject> listUser = new ArrayList<>();


        for (JsonObject user : listAllUser) {
            listUser.add(user);
            if (listUser.size() == 20 || listAllUser.indexOf(user) == listAllUser.size() - 1) {
                try {
                    createMultipleUsersInOneRequest(listUser);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                listUser.clear();
            }
        }
        logger.getHandlers()[0].close();

    }


    public void assignLicense(String userId) {
        String skuId = "c42b9cae-ea4f-4ab7-9717-81576235ccac"; // SKU ID của giấy phép muốn gán
        String requestUrl = "https://graph.microsoft.com/v1.0/users/" + userId + "/assignLicense";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Tạo payload JSON
            String requestBody = "{ \"addLicenses\": [{\"disabledPlans\":[],\"skuId\":\"" + skuId + "\"}],\"removeLicenses\":[]}";
            con.getOutputStream().write(requestBody.getBytes("utf-8"));

            // Ghi payload vào OutputStream và gửi yêu cầu
            int responseCode = con.getResponseCode();

            Gson gson = new GsonBuilder().create();
            BufferedReader in;
            if (responseCode >= 400) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    errorResponse.append(line);
                }
                // Parse the JSON string
                JsonObject jsonObject = gson.fromJson(errorResponse.toString(), JsonObject.class);
                // Get the value of the "message" property
                if (jsonObject.getAsJsonObject("error").get("code").getAsString().equals("Request_ResourceNotFound")){
                    System.out.println("User does not exist");
                }
                String message = jsonObject.getAsJsonObject("error").get("message").getAsString();
                // Print the error message
                System.out.println("Error: " + message);
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                System.out.println("Assign license success");
                System.out.println(in.readLine());
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
