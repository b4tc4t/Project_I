import com.google.gson.JsonObject;
import template.service.GroupServiceImpl;
import template.service.JsonTool;
import template.service.airtable.Table;
import template.team_config.config;

import java.util.List;

public class TaskScheduleForAntiRicons {
    public static void main(String[] args) throws Exception{


        String token = config.getAccessToken();
        String tableId = JsonTool.getAccessInfo("src/main/java/infoTaskSchedule.json").get("tableId").getAsString();
        String groupId = JsonTool.getAccessInfo("src/main/java/infoTaskSchedule.json").get("groupId").getAsString();

        List<JsonObject> fields = GroupServiceImpl.listUsersAsJson(groupId, token);
        //token Airtable
        JsonObject access = JsonTool.getAccessInfo("template/service/airtable/configAirTable.json").getAsJsonObject();
        String personal_access_token = access.get("personal_access_token").getAsString();
        String baseId = access.get("baseId").getAsString();

        JsonObject fieldTable = Table.getTable(tableId, baseId, personal_access_token);

        Table table = new Table(fieldTable, baseId, personal_access_token);
        table.pullAllRecords(fields, baseId, personal_access_token);

    }


}