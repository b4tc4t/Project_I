import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import template.jsonUtil.JsonTool;
import template.persistence.dto.Group;
import template.persistence.dto.User;
import template.service.*;

import template.colorUtil.Color;
import template.service.airtable.Table;

import template.team_config.Config;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Scanner;

public class CLI {

    public static void showMenu() {
        String configAirTable = Config.getConfigAirTable();

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
                "4. Create Group\n" +
                "5. Create Team\n" +
                "6. Delete Team(Group)\n" +
                "7. Create Channel\n" +
                "8. Add user/multiple users to Team\n" +
                "9. Get user by principal name\n" +
                "10. Assign license for user\n" +
                "11. List existed groupIDs\n" +
                "12. List existed teams\n" +
                "13. Create link to team\n" +

                "--Airtable--\n" +
                "14. Synchronize Users information in an organization to Airtable \n" +
                "15. Synchronize Users information in a Team to Airtable\n" +
                "16. Write to XLSX file\n" +
                "----------------------------------------------\n" +
                "17. Print help\n" +
                "18. Exit";
        try {
            while (true) {
                Color.printGreen(general);
                Scanner input = new Scanner(System.in);
                String option = input.nextLine();

                UserServiceImpl service = new UserServiceImpl();
                GroupServiceImpl groupService = new GroupServiceImpl();

                if (option.equals("1")) {

                    Color.printCyan("-- Create user -- ");

                    Color.printCyan("Enter displayName: ");
                    String displayName = input.nextLine();

                    Color.printCyan("Enter mailNickname: ");
                    String mailNickname = input.nextLine();

                    Color.printCyan("Enter userPrincipalName: ");
                    String userPrincipalName = input.nextLine();

                    Color.printCyan("Enter password: ");
                    String password = input.nextLine();

                    User user = new User(displayName, mailNickname, userPrincipalName, password);
                    service.createUser(user);
                } else if (option.equals("2")) {

                    Color.printCyan("-- Create multiple users from csv file --");

                    Color.printCyanNo("Enter filepath: ");
                    String path = input.nextLine();

                    service.createAllUsers(path);
                } else if (option.equals("3")) {
                    Color.printCyan("-- Delete user from organization --");

                    Color.printCyanNo("Enter userId: ");
                    String userId = input.nextLine();

                    service.deleteUser(userId);
                } else if (option.equals("4")) {
                    Color.printCyan("-- Create Group --");

                    Color.printCyanNo("Enter displayName: ");
                    String displayName = input.nextLine();

                    Color.printCyanNo("Enter description: ");
                    String description = input.nextLine();

                    Color.printCyanNo("Enter mailNickname: ");
                    String mailNick = input.nextLine();

                    Color.printCyanNo("Enter ownerId: ");
                    String ownerId = input.nextLine();

                    Color.printCyanNo("Enter userIds: ");
                    List<String> userId = new ArrayList<>();
                    String line = input.nextLine();
                    while (!line.isEmpty()) {
                        userId.add(line);
                        line = input.nextLine();
                    }

                    Group group = new Group(displayName, mailNick, ownerId);

                    groupService.createGroup(group, ownerId, userId);


                } else if (option.equals("5")) {
                    Color.printCyan("-- Create Team --");

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    groupService.createTeam(groupId);
                } else if (option.equals("6")) {
                    Color.printCyan("-- Delete Team(Group) --");

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    groupService.deleteTeam(groupId);
                } else if (option.equals("7")) {
                    Color.printCyan("-- Create Channel --");

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    Color.printCyanNo("Enter displayName: ");
                    String displayName = input.nextLine();

                    Color.printCyanNo("Enter description: ");
                    String description = input.nextLine();

                    ChannelService.createChannel(groupId, displayName, description);
                } else if (option.equals("8")) {
                    Color.printCyan("-- Add user/multiple users to Team --");

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    Color.printCyanNo("Enter userIds: ");
                    List<String> userId = new ArrayList<>();
                    String line = input.nextLine();
                    while (!line.isEmpty()) {
                        userId.add(line);
                        line = input.nextLine();
                    }

                    groupService.addMemberToTeam(groupId, userId);
                } else if (option.equals("9")) {
                    Color.printCyan("-- Get user by principal name --");

                    Color.printCyanNo("Enter userPrincipalName (example@example.com): ");
                    String userPrincipalName = input.nextLine();

                    service.getUserByPrincipalName(userPrincipalName);
                } else if (option.equals("10")) {
                    Color.printCyan("-- Assign license for user --");

                    Color.printCyanNo("Enter userId: ");
                    String userId = input.nextLine();

                    Color.printCyanNo("Enter userId: ");
                    String skuId = input.nextLine();
                    service.assignLicense(userId, skuId);
                } else if (option.equals("11")) {
                    Color.printCyan("-- List existed groupId --");

                    groupService.listIDsGroup();
                } else if (option.equals("12")) {
                    Color.printCyan("-- List existed Teams --");

                    List<String[]> idAndNameList = groupService.listIDsTeam();

                    for (String[] idAndName : idAndNameList) {
                        Color.printBlue("ID: " + idAndName[0] + ", name: " + idAndName[1]);
                    }
                } else if (option.equals("13")) {
                    Color.printCyan("-- Create link to team --");

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    String linkToTeam = groupService.createLinkToTeam(groupId);
                    if (linkToTeam.contains("Invalid group ID") || linkToTeam.contains("Group not found")) {
                        Color.printCyan(linkToTeam);
                    } else {
                        Color.printBlue(linkToTeam);
                    }

                } else if (option.equals("14")) {
                    Color.printCyan("-- Synchronize Users information in an organization to Airtable --");

                    // token MS
                    String token = Config.getAccessToken();


                    List<JsonObject> fields = Organization.listUsersAsJson(token);

                    //token Airtable
                    JsonObject access = JsonTool.getAccessInfo(configAirTable).getAsJsonObject();
                    String personal_access_token = access.get("personal_access_token").getAsString();
                    String baseId = access.get("baseId").getAsString();

                    Color.printCyan("Do you want to synchronize to an existed table (Press 1) or create new table and sync (Press 2)?");
                    String choose = input.nextLine();
                    if (choose.equals("1")) {
                        Color.printCyanNo("Enter tableId: ");
                        String tableId = input.nextLine();

                        JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

                        if (fieldTable != null) {
                            Table table = new Table(fieldTable, baseId, personal_access_token);
                            table.pullAllRecords(fields, baseId, personal_access_token);
                        } else {
                            Color.printYellow("Error: Invalid tableId");
                        }
                    } else if (choose.equals("2")) {
                        Color.printCyanNo("Enter table name: ");
                        String name = input.nextLine();

                        JsonArray fieldSetting = Organization.tableSetting;

                        String response = Table.createTable(name, fieldSetting, baseId, personal_access_token);
                        if (response != null) {
                            JsonObject table = JsonParser.parseString(response).getAsJsonObject();

                            Table tableObj = new Table(table, baseId, personal_access_token);
                            tableObj.pullAllRecords(fields, baseId, personal_access_token);
                        } else {
                            Color.printYellow("Error: Invalid tableId");
                        }
                    }
                } else if (option.equals("15")) {
                    Color.printCyan("-- Synchronize Users information in an Team(Group) to Airtable --");

                    // token MS
                    String token = Config.getAccessToken();

                    Color.printCyanNo("Enter groupId: ");
                    String groupId = input.nextLine();

                    List<JsonObject> fields = GroupServiceImpl.listUsersAsJson(groupId, token);
                    if (fields != null) {


                        //token Airtable
                        JsonObject access = JsonTool.getAccessInfo(configAirTable).getAsJsonObject();
                        String personal_access_token = access.get("personal_access_token").getAsString();
                        String baseId = access.get("baseId").getAsString();

                        Color.printCyan("Do you want to synchronize to an existed table (Press 1) or create new table and sync (Press 2)?");
                        String choose = input.nextLine();
                        if (choose.equals("1")) {
                            Color.printCyanNo("Enter tableId: ");
                            String tableId = input.nextLine();

                            JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);
                            if (fieldTable != null) {
                                Table table = new Table(fieldTable, baseId, personal_access_token);
                                table.pullAllRecords(fields, baseId, personal_access_token);
                            } else {
                                Color.printYellow("Invalid talbe ID");
                            }
                        } else if (choose.equals("2")) {
                            Color.printCyanNo("Enter table name: ");
                            String name = input.nextLine();

                            JsonArray fieldSetting = GroupServiceImpl.tableSetting;

                            String response = Table.createTable(name, fieldSetting, baseId, personal_access_token);
                            if (response != null) {
                                JsonObject fieldTable = JsonParser.parseString(response).getAsJsonObject();

                                if (fieldTable != null) {
                                    Table table = new Table(fieldTable, baseId, personal_access_token);
                                    table.pullAllRecords(fields, baseId, personal_access_token);
                                } else {
                                    Color.printYellow("Error: Invalid tableId");
                                }
                            } else {
                                Color.printYellow("Error: Invalid tableId");
                            }
                        }
                    } else {
                        Color.printYellow("Group ID not found");

                    }
                } else if (option.equals("16")) {
                    Color.printCyan("-- Write table to XLSX file --");

                    //token Airtable
                    JsonObject access = JsonTool.getAccessInfo(configAirTable).getAsJsonObject();
                    String personal_access_token = access.get("personal_access_token").getAsString();
                    String baseId = access.get("baseId").getAsString();

                    Color.printCyanNo("Enter tableId: ");
                    String tableId = input.nextLine();

                    Color.printCyanNo("Enter filepath/filename: ");
                    String filepath = input.nextLine();

                    JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

                    if (fieldTable != null) {
                        Table table = new Table(fieldTable, baseId, personal_access_token);

                        table.writeTableToXLSX(filepath, baseId, personal_access_token);
                    } else {
                        Color.printYellow("Error: Invalid tableId");
                    }
                } else if (option.equals("17")) {
                    Color.printGreen(general);
                } else if (option.equals("18")) {
                    break;
                } else {
                    continue;
                }

                Color.printGreen("Do you want to continue using ANTI-RICONS application? Y/N");
                String choosing = input.nextLine();
                if (!choosing.equals("Y")) {
                    break;
                }
            }
        } catch (IOException |
                 InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Oops! We ran into some problems");
        }

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        showMenu();
    }
}

