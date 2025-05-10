package edu.vinu.springboot_websocket.service;

import edu.vinu.springboot_websocket.model.User;

import java.util.List;

public interface UserService {
    void registerUser(User user);

    List<User> getAllUsers();

    User getUserByUsername(String userName);

    void userJoin(String username);

    List<User> getAllOtherUsers(String username);
}
