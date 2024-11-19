package template.service;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONObject;
import template.colorUtil.Color;
import template.persistence.dto.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static template.team_config.Config.getAccessToken;

public class UserServiceImpl implements UserService {
    private String token = getAccessToken();
    //private static final Logger logger = Logger.getLogger("MyLog");
    //private static final Logger logger = Diary.getLogger();

    public UserServiceImpl() throws IOException, InterruptedException {
        // TODO document why this constructor is empty
    }


    public void getUserByPrincipalName(String principalName) {
        String graphEndpoint = String.format("https://graph.microsoft.com/v1.0/users/%s", principalName);
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(graphEndpoint).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            //System.out.println(responseCode);
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
                Color.printBlue(resultString);
            } else if (con.getResponseCode() == 404) {
                Color.printYellow("User not found or wrong userPrincipalName format ,please enter userPrincipalName refer to your organization");
            } else if (con.getResponseCode() == 400) {
                Color.printYellow("Invalid character appear in userPrincipalName");
                //System.out.println("Request failed with response code: " + con.getResponseCode());
            }

        } catch (IOException e) {
            System.out.println("Exception error: " + e);
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
            Color.printYellow("Error: " + message);
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            Color.printBlue("Create user success");
            Color.printBlue(in.readLine());
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
            Color.printBlue("User deleted successfully.");
        } else {
            // Handle the error response
            Color.printYellow("Error deleting user: " + con.getResponseMessage());
        }

    }

    public static void createMultipleUsersInOneRequest(List<JsonObject> listUser) throws IOException, InterruptedException {
        try (FileWriter writer = new FileWriter("diary.txt", true)) {


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
                writer.write("Error: " + message + "\n");
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                writer.write("Batch request fetch success :\n");
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
                        writer.write("User Principal Name : " + listUser.get(respIdInt).get("userPrincipalName") + "\n" +
                                "Response Status : " + respStatus + "\n" +
                                "Error : " + message + "\n");
                    } else {
                        writer.write("User Principal Name : " + listUser.get(respIdInt).get("userPrincipalName") + "\n" +
                                "Response Status : " + respStatus + "\n" +
                                "Create user success\n");
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error writing to diary.txt: " + e.getMessage());
        }
    }

    public void createAllUsers(String path) throws IOException {
        File file = new File("diary.txt");
        boolean fileExists = file.exists();
        if (!fileExists) {
            try {
                fileExists = file.createNewFile();
                if (fileExists) {
                    Color.printBlue("File diary.txt created successfully.");
                } else {
                    Color.printYellow("Failed to create file diary.txt.");
                }
            } catch (IOException e) {
                System.out.println("Exception error creating file diary.txt: " + e.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter("diary.txt", true)) {
            // Add timestamp to file
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.write("Creating multiple users in one request at " + timestamp + "...\n");
        }
        List<JsonObject> listAllUser = new ArrayList();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));
            List<String[]> rows = reader.readAll();
            Iterator var5 = rows.iterator();

            while (var5.hasNext()) {
                String[] row = (String[]) var5.next();
                JsonObject user = new JsonObject();
                user.addProperty("accountEnabled", true);
                user.addProperty("displayName", row[0]);
                user.addProperty("mailNickname", row[1]);
                user.addProperty("userPrincipalName", row[2]);
                JsonObject passwordProfile = new JsonObject();
                passwordProfile.addProperty("forceChangePasswordNextSignIn", true);
                passwordProfile.addProperty("password", row[3]);
                user.add("passwordProfile", passwordProfile);
                listAllUser.add(user);
            }
        } catch (IOException | CsvException var11) {
            Color.printYellow("File not found ");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Exception error: " + e);
                }
            }
            List<JsonObject> listUser = new ArrayList();
            int lastIndexProcessed = -1;
            int successfulCreations = 0;
            while (lastIndexProcessed < listAllUser.size() - 1) {
                for (int i = lastIndexProcessed + 1; i < listAllUser.size(); i++) {

                    JsonObject user = listAllUser.get(i);
                    listUser.add(user);
                    if (listUser.size() == 20 || i == listAllUser.size() - 1) {
                        try {
                            createMultipleUsersInOneRequest(listUser);
                            successfulCreations += listUser.size();
                            Color.printBlue(successfulCreations + " users created successfully!");
                        } catch (InterruptedException | IOException var9) {
                            System.out.println("Exception error: " + var9);
                            Thread.currentThread().interrupt();
                            break;
                        }

                        listUser.clear();
                        lastIndexProcessed = i;
                        break;
                    }
                }

            }
            FileWriter writer = null;
            try {
                writer = new FileWriter("diary.txt", true);
                writer.write("\n");
                writer.write("=========================================================================================================================\n");
                writer.write("\n");
                writer.close();
            } catch (Exception e) {

            }
        }
    }

    public void assignLicense(String userId, String skuId) {
        //String skuId = "c42b9cae-ea4f-4ab7-9717-81576235ccac"; // SKU ID của giấy phép muốn gán
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

                // Check if the "error" field is a JSON object
                JsonElement errorElement = jsonObject.get("error");
                if (errorElement != null && errorElement.isJsonObject()) {
                    JsonObject errorObject = errorElement.getAsJsonObject();
                    String code = errorObject.get("code").getAsString();
                    if (code.equals("Request_ResourceNotFound")) {
                        Color.printYellow("User does not exist");
                    }
                    String message = errorObject.get("message").getAsString();
                    if (message.contains(" to the expected type 'Edm.Guid'")) {
                        Color.printYellow("skuID not found");
                    } else {
                        Color.printYellow("Error: " + message);
                    }
                    //System.out.println("Error: " + message);
                } else {
                    Color.printYellow("Error: " + jsonObject.toString());
                }
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Color.printBlue("Assign license success");
                Color.printBlue(in.readLine());
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Exception error: " + e);
        } catch (JsonSyntaxException e) {
            System.out.println("Exception error parsing JSON response: " + e.getMessage());
        }
    }


    public String listJoinedTeams(String userId) throws IOException, InterruptedException {
        String graphEndpoint = "https://graph.microsoft.com/v1.0/users/" + userId + "/joinedTeams";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(graphEndpoint))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonArray arrayValue = new JsonArray();
        if (jsonObject.has("value")) {
            arrayValue = jsonObject.get("value").getAsJsonArray();
        }

        return arrayValue.toString();
    }

}
