package com.example.springexample.service;

import com.example.springexample.Exceptions.PasswordIsNotCorrect;
import com.example.springexample.Exceptions.UserNotFoundException;
import com.example.springexample.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users;
    private User currentUser;

    public UserService() {
        this.users = new HashMap<>();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username " + username + " is already registered");
        }
        users.put(username, new User(username, password));
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException("Username " + username + " not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new PasswordIsNotCorrect("Invalid password");
        }
        currentUser = user;
        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout(){
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}
