package template;

import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
public class testcsv {
    private static final Random rand = new Random();

    // Define the number of users to create
    private static final int NUM_USERS = 50;

    public static void main(String[] args) {
        // Create a new CSV file
        String filePath = "users.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the header row
            writer.write("Display Name,Mail Nickname,User Principal Name,Password\n");

            // Generate the specified number of users
            for (int i = 0; i < NUM_USERS; i++) {
                // Generate random values for each user property
                String displayName = "User " + (i+1);
                String mailNickname = "user" + (i+1);
                String userPrincipalName = mailNickname + "@3pjv85.onmicrosoft.com";
                String password = generateRandomPassword();

                // Create a new JSON object to represent the user
                JsonObject user = new JsonObject();
                user.addProperty("accountEnabled", true);
                user.addProperty("displayName", displayName);
                user.addProperty("mailNickname", mailNickname);
                user.addProperty("userPrincipalName", userPrincipalName);
                JsonObject passwordProfile = new JsonObject();
                passwordProfile.addProperty("forceChangePasswordNextSignIn", true);
                passwordProfile.addProperty("password", password);
                user.add("passwordProfile", passwordProfile);

                // Write the user to the CSV file
                writer.write(displayName + "," + mailNickname + "," + userPrincipalName + "," + password + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate a random 8-character password
    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = rand.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
