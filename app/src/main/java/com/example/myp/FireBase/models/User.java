package com.example.myp.FireBase.models;

import android.os.Parcel;
import android.os.Parcelable;

// FireBase Class to register new users in the DataBase
public class User implements Parcelable {
    private String email;
    private String password;
    private String user_full_name;
    private String phone;
    private String gender;
    private String user_id;

    public User(String email, String password, String user_full_name, String phone, String gender, String user_id) {
        this.email = email;
        this.password = password;
        this.user_full_name = user_full_name;
        this.phone = phone;
        this.gender = gender;
        this.user_id = user_id;
    }

    public User() {

    }

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        user_full_name = in.readString();
        phone = in.readString();
        gender = in.readString();
        user_id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserFullName() {
        return user_full_name;
    }
    public void setUserFullName(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return user_id;
    }
    public void setUserID(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_full_name='" + user_full_name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(user_full_name);
        dest.writeString(phone);
        dest.writeString(gender);
        dest.writeString(user_id);
    }
}
