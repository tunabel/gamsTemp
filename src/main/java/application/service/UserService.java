package application.service;

import application.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    List<User> findAllByOrderById();

    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(String id);
}
