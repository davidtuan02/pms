package com.example.mentalhealthcarepms.dto;

import com.example.mentalhealthcarepms.entity.User;

import java.util.List;

public class UsersResponse extends ResponseObject {
    private List<User> users;
    private User user;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
