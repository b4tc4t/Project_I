package template.service;

import java.io.IOException;

public interface UserService {
    void getUser() throws IOException, InterruptedException;
    void createUser(String displayName ,String mailNickname ,String userPrincipalName ,String password) throws IOException, InterruptedException;
}
