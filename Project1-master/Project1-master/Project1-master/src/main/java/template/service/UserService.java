package template.service;

import template.persistence.dto.User;

import java.io.IOException;

public interface UserService {
    void createUser(User newUser) throws IOException, InterruptedException;
    void deleteUser(String id) throws IOException, InterruptedException;
    void assignLicense(String userId,String skuID);
    void createAllUsers(String path) throws IOException;
    void getUserByPrincipalName(String pricipalName);

}
