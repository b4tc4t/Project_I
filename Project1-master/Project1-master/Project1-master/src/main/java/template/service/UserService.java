package template.service;

import template.persistence.dto.User;

import java.io.IOException;

public interface UserService {
    void getUser() throws IOException, InterruptedException;
    void createUser(User newUser) throws IOException, InterruptedException;
    void deleteUser(String id) throws IOException, InterruptedException;
    void assignLicense(String userId);
    void createAllUsers(String path);
    void getUserByPrincipalName(String pricipalName);

}
