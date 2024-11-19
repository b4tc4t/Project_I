
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import template.persistence.dto.Group;
import template.persistence.dto.User;
import template.service.GroupService;
import template.service.GroupServiceImpl;
import template.service.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static template.api_config.config.getAccessToken;


public class Main {
    public static void main(String[] args) throws Exception{
        /*
        UserServiceImpl service = new UserServiceImpl();

        //test get user
        service.getUser();*/
        /*
        //test create user
        User user = new User("John Doe" , "johndoe" ,"johndoe@3pjv85.onmicrosoft.com" ,"P@ssw0rd123");
        service.createUser(user);
        */
        /*
        //test get user by principal name
        String principalName = "hapiH@3pjv85.onmicrosoft.com";
        service.getUserByPrincipalName(principalName);
        */
        /*
        //test delete user
        service.deleteUser("97b98bf3-0b61-4c7f-bc3c-55bc997239b2");
         */

        /*
        //test create multiple user from csv
        service.createAllUsers("C:\\Users\\LamPhuss\\Downloads\\Project1-master\\users.csv");
        */
        /*
        //test assign licence
        service.assignLicense("5d22e230-ac9a-4d26-9ed8-ebc4014527");

        */
        /*
        GroupServiceImpl service2 = new GroupServiceImpl();

        //test create group
        String des = "Group with designated owner and members";

        String display = "My group";
        String mailNick = "phusgroup2023";
        String ownerId = "66ebefba-651f-4fe7-ab39-2b3a29b5643c";
        Group group = new Group(display,mailNick,ownerId);
        List<String> userIds = new ArrayList<>();
        userIds.add("6301cd42-30ba-476e-aca5-ad1fae558b0b");
        userIds.add("88ff6ec2-3b5d-4b56-b013-ebd683870604");
        service2.createGroup(group,ownerId,userIds);
        */
        /*
        //test create team
        service2.createTeam("1e7ba4f2-5af5-47df-af68-394a5d62951f");
        */
        /*
        //test delete team
        service2.deleteTeam("145f7aad-9517-48ec-b589-0d9e51f6ceff");
           */
        /*
        //test add user to group
        String groupId = "1e7ba4f2-5af5-47df-af68-394a5d62951f"; // Replace with your actual group ID
        List<String> memberIds = List.of("3eb3c149-d420-4fb3-a00a-f46217a15920", "5d22e230-ac9a-4d26-9ed8-ebc401454827");
        service2.addMemberToTeam(groupId,memberIds);*/
        /*
        //test add user to group
        String groupId = "1e7ba4f2-5af5-47df-af68-394a5d62951f"; // Replace with your actual group ID
        List<String> memberIds = List.of("3eb3c149-d420-4fb3-a00a-f46217a15920", "5d22e230-ac9a-4d26-9ed8-ebc401454827");
        service2.addMemberToTeam(groupId,memberIds);*/

    }


}