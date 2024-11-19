import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.graph.models.User;

import com.microsoft.graph.requests.GraphServiceClient;
import org.apache.http.HttpRequest.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import template.service.UserServiceImpl;

import javax.net.ssl.HttpsURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpRequest.*;

public class Main {
    public static void main(String[] args) throws Exception{
        UserServiceImpl service = new UserServiceImpl();
        /*
        //test get user
        service.getUser();*/
        /*
        //test create user
        service.createUser("John Doe" , "johndoe" ,"johndoe@3pjv85.onmicrosoft.com" ,"P@ssw0rd123");*/


    }
}