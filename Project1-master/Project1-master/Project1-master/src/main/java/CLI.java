import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import template.team_config.config;
import template.persistence.dto.Group;
import template.persistence.dto.User;
import template.service.*;

import template.colorUtil.Color;
import template.service.airtable.Table;


import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Scanner;

public class CLI {

    public static void main(String[] args) throws IOException, InterruptedException {
        String general = "----------------------------------------------\n" +
                "            ANTI-RICONS Application\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣤⣤⣤⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⠟⠉⠉⠉⠉⠉⠉⠉⠙⠻⢶⣄⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⠙⣷⡀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⡟⠀⣠⣶⠛⠛⠛⠛⠛⠛⠳⣦⡀⠀⠘⣿⡄⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⠁⠀⢹⣿⣦⣀⣀⣀⣀⣀⣠⣼⡇⠀⠀⠸⣷⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡏⠀⠀⠀⠉⠛⠿⠿⠿⠿⠛⠋⠁⠀⠀⠀⠀⣿\n" +
                "⠀⠀      ⢠⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⡇⠀\n" +
                "        ⣸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣧⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⢸⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⣾⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⣿⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⠀⠀⠀⠀⣿⠀\n" +
                "⠀⠀⠀⠀⠀⠀⢰⣿⠀⠀⠀⠀⣠⡶⠶⠿⠿⠿⠿⢷⣦⠀⠀⠀⠀⠀    ⣿⠀\n" +
                "⠀⠀⣀⣀⣀⠀⣸⡇⠀⠀⠀⠀⣿⡀⠀⠀⠀⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠀  ⣿⠀\n" +
                "⣠⡿⠛⠛⠛⠛⠻⠀⠀⠀⠀⠀⢸⣇⠀⠀⠀⠀⠀⠀⣿⠇⠀⠀⠀⠀⠀ ⠀ ⣿⠀\n" +
                "⢻⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⡟⠀⠀⢀⣤⣤⣴⣿⠀⠀⠀⠀⠀⠀  ⠀⣿⠀\n" +
                "⠈⠙⢷⣶⣦⣤⣤⣤⣴⣶⣾⠿⠛⠁⢀⣶⡟⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡟⠀\n" +
                "                ⠈⣿⣆⡀⠀⠀⠀⠀⠀⠀⢀⣠⣴⡾⠃⠀\n" +
                "⠀                ⠈⠛⠻⢿⣿⣾⣿⡿⠿⠟⠋⠁⠀⠀⠀\n" +
                "How can I help you? Please choose an option:\n" +
                "--MS Teams--\n" +
                "1. Create user\n" +
                "2. Create multiple users from csv file\n" +
                "3. Delete user from organization\n" +
                "4. Create Team\n" +
                "5. Delete Team(Group)\n" +
                "6. Create Channel\n" +
                "7. Add user/multiple users to Team\n" +
                "8. Get user by principal name\n" +
                "9. Assign license for user\n" +

                "--Airtable--\n" +
                "10. Synchronize Users information in an organization to Airtable \n" +
                "11. Synchronize Users information in a Team to Airtable\n" +
                "12. Write to XLSX file\n" +
                "----------------------------------------------\n" +
                "13. Print help\n" +
                "14. Exit";

        while (true)
        {
            Color.print_green(general);
            Scanner input = new Scanner(System.in);
            String option = input.nextLine();

            UserServiceImpl service = new UserServiceImpl();
            GroupServiceImpl groupService = new GroupServiceImpl();

            if (option.equals("1"))
            {

                Color.print_yellow("-- Create user -- ");

                Color.print_yellow("Enter displayName: ");
                String displayName = input.nextLine();

                Color.print_yellow("Enter mailNickname: ");
                String mailNickname = input.nextLine();

                Color.print_yellow("Enter userPrincipalName: ");
                String userPrincipalName = input.nextLine();

                Color.print_yellow("Enter password: ");
                String password = input.nextLine();

                User user = new User(displayName, mailNickname, userPrincipalName, password);
                service.createUser(user);
            }
            else if (option.equals("2"))
            {

                Color.print_yellow("-- Create multiple users from csv file --");

                Color.print_yellow_no("Enter filepath: ");
                String path = input.nextLine();

                service.createAllUsers(path);
            }
            else if (option.equals("3"))
            {
                Color.print_yellow("-- Delete user from organization --");

                Color.print_yellow_no("Enter userId: ");
                String userId = input.nextLine();

                service.deleteUser(userId);
            }
            else if (option.equals("4"))
            {
                Color.print_yellow("-- Create Team --");

                Color.print_yellow_no("Enter displayName: ");
                String displayName = input.nextLine();

                Color.print_yellow_no("Enter description: ");
                String description = input.nextLine();

                Color.print_yellow_no("Enter mailNickname: ");
                String mailNick = input.nextLine();

                Color.print_yellow_no("Enter ownerId: ");
                String ownerId = input.nextLine();

                Color.print_yellow_no("Enter userIds: ");
                List<String> userId = new ArrayList<>();
                String line = input.nextLine();
                while (!line.isEmpty()) {
                    userId.add(line);
                    line = input.nextLine();
                }

                Group group = new Group(displayName, mailNick, ownerId);

                String groupId = groupService.createGroup(group,ownerId,userId);

                groupService.createTeam(groupId);
            }
            else if (option.equals("5"))
            {
                Color.print_yellow("-- Delete Team(Group) --");

                Color.print_yellow_no("Enter groupId: ");
                String groupId = input.nextLine();

                groupService.deleteTeam(groupId);
            }
            else if (option.equals("6"))
            {
                Color.print_yellow("-- Create Channel --");

                Color.print_yellow_no("Enter groupId: ");
                String groupId = input.nextLine();

                Color.print_yellow_no("Enter displayName: ");
                String displayName = input.nextLine();

                Color.print_yellow_no("Enter description: ");
                String description = input.nextLine();

                ChannelService.create(groupId, displayName, description);
            }
            else if (option.equals("7"))
            {
                Color.print_yellow("-- Add user/multiple users to Team --");

                Color.print_yellow_no("Enter groupId: ");
                String groupId = input.nextLine();

                Color.print_yellow_no("Enter userIds: ");
                List<String> userId = new ArrayList<>();
                String line = input.nextLine();
                while (!line.isEmpty()) {
                    userId.add(line);
                    line = input.nextLine();
                }

                groupService.addMemberToTeam(groupId, userId);
            }
            else if (option.equals("8"))
            {
                Color.print_yellow("-- Get user by principal name --");

                Color.print_yellow_no("Enter userPrincipalName (example@example.com): ");
                String userPrincipalName = input.nextLine();

                service.getUserByPrincipalName(userPrincipalName);
            }
            else if (option.equals("9"))
            {
                Color.print_yellow("-- Assign license for user --");

                Color.print_yellow_no("Enter userId: ");
                String userId = input.nextLine();

                service.assignLicense(userId);
            }
            else if (option.equals("10"))
            {
                Color.print_yellow("-- Synchronize Users information in an organization to Airtable --");

                // token MS
                String token = config.getAccessToken();


                List<JsonObject> fields = Organization.listUsersAsJson(token);

                //token Airtable
                JsonObject access = JsonTool.getAccessInfo("template/service/airtable/configAirTable.json").getAsJsonObject();
                String personal_access_token = access.get("personal_access_token").getAsString();
                String baseId = access.get("baseId").getAsString();

                Color. print_yellow("Do you want to synchronize to an existed table (Press 1) or create new table and sync (Press 2)?");
                String choose = input.nextLine();
                if (choose.equals("1"))
                {
                    Color.print_yellow_no("Enter tableId: ");
                    String tableId = input.nextLine();

                    JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

                    Table table = new Table(fieldTable, baseId, personal_access_token);
                    table.pullAllRecords(fields, baseId, personal_access_token);
                }
                else if (choose.equals("2"))
                {
                    Color.print_yellow_no("Enter table name: ");
                    String name = input.nextLine();

                    JsonArray fieldSetting = Organization.tableSetting;

                    String response = Table.createTable(name, fieldSetting, baseId, personal_access_token);

                    JsonObject table = JsonParser.parseString(response).getAsJsonObject();

                    Table tableObj = new Table(table, baseId, personal_access_token);
                    tableObj.pullAllRecords(fields, baseId, personal_access_token);
                }
                }
            else if (option.equals("11"))
            {
                Color.print_yellow("-- Synchronize Users information in an Team(Group) to Airtable --");

                // token MS
                String token = config.getAccessToken();

                Color.print_yellow_no("Enter groupId: ");
                String groupId = input.nextLine();

                List<JsonObject> fields = GroupServiceImpl.listUsersAsJson(groupId, token);

                //token Airtable
                JsonObject access = JsonTool.getAccessInfo("template/service/airtable/configAirTable.json").getAsJsonObject();
                String personal_access_token = access.get("personal_access_token").getAsString();
                String baseId = access.get("baseId").getAsString();

                Color. print_yellow("Do you want to synchronize to an existed table (Press 1) or create new table and sync (Press 2)?");
                String choose = input.nextLine();
                if (choose.equals("1"))
                {
                    Color.print_yellow_no("Enter tableId: ");
                    String tableId = input.nextLine();

                    JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

                    Table table = new Table(fieldTable, baseId, personal_access_token);
                    table.pullAllRecords(fields, baseId, personal_access_token);
                }
                else if (choose.equals("2"))
                {
                    Color.print_yellow_no("Enter table name: ");
                    String name = input.nextLine();

                    JsonArray fieldSetting = GroupServiceImpl.tableSetting;

                    String response = Table.createTable(name, fieldSetting, baseId, personal_access_token);

                    JsonObject table = JsonParser.parseString(response).getAsJsonObject();

                    Table tableObj = new Table(table, baseId, personal_access_token);
                    tableObj.pullAllRecords(fields, baseId, personal_access_token);
                }
            }
            else if (option.equals("12"))
            {
                Color.print_yellow("-- Write table to XLSX file --");

                //token Airtable
                JsonObject access = JsonTool.getAccessInfo("template/service/airtable/configAirTable.json").getAsJsonObject();
                String personal_access_token = access.get("personal_access_token").getAsString();
                String baseId = access.get("baseId").getAsString();

                Color.print_yellow_no("Enter tableId: ");
                String tableId = input.nextLine();

                Color.print_yellow_no("Enter filepath/filename: ");
                String filepath = input.nextLine();

                JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

                Table table = new Table(fieldTable, baseId, personal_access_token);

                table.writeTableToXLSX(filepath, baseId, personal_access_token);
            }
            else if (option.equals("13"))
            {
                Color.print_green(general);
            }
            else if (option.equals("14"))
            {
                break;
            }
            Color.print_green("Do you want to continue using ANTI-RICONS application? Y/N");
            String choosing = input.nextLine();
            if (choosing.equals("Y"))
            {
                continue;
            }
            else
            {
                break;
            }
        }
    }
}

