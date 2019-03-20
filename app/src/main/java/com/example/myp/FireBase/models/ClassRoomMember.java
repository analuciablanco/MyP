package com.example.myp.FireBase.models;

import android.os.Parcel;
import android.os.Parcelable;

// FireBase Class to add Parents and Teachers to a ClassRoom
public class ClassRoomMember implements Parcelable{

    private String user_full_name;
    private String member_id;
    private String user_id;
    private String role;
    private String member_status;

    private ClassRoomMember(String user_full_name,String member_id,String user_id, String role, String member_status){
        this.user_full_name = user_full_name;
        this.member_id = member_id;
        this.user_id = user_id;
        this.role = role;
        this.member_status = member_status;
    }
    public ClassRoomMember(){

    }

    protected ClassRoomMember (Parcel in){
        user_full_name = in.readString();
        member_id = in.readString();
        user_id = in.readString();
        role = in.readString();
        member_status = in.readString();
    }

    public static final Creator<ClassRoomMember> CREATOR = new Creator<ClassRoomMember>() {
        @Override
        public ClassRoomMember createFromParcel(Parcel in) {
            return new ClassRoomMember(in);
        }

        @Override
        public ClassRoomMember[] newArray(int size) {
            return new ClassRoomMember[size];
        }
    };

    public String getUser_full_name(){return  user_full_name;}
    public void  setUser_full_name(String user_full_name){this.user_full_name = user_full_name;}

    public String  getMember_id (){
        return member_id;
    }
    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getUser_id (){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public  String getRole(){
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getMember_status(){return  member_status;}
    public void  setMember_status(String member_status){this.member_status = member_status;}

    @Override
    public String toString(){
        return "classroomMembers{" +
                "memberID='" + member_id + '\'' +
                ",userID='" + user_id + '\'' +
                ",member_role='" + role + '\'' +
                ",member_status='" + member_status + '\'' +
                ",user_full_name='" + user_full_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_full_name);
        dest.writeString(member_id);
        dest.writeString(user_id);
        dest.writeString(role);
        dest.writeString(member_status);
    }
}