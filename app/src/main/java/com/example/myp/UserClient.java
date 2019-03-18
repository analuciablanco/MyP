package com.example.myp;

import android.app.Application;

import com.example.myp.FireBase.models.User;

public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
