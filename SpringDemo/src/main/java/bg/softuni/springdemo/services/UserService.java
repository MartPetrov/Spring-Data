package bg.softuni.springdemo.services;

import bg.softuni.springdemo.models.User;

public interface UserService {
    void register(String username, int age);


    User findByUsername(String username);
}
