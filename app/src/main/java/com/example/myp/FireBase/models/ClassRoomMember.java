package com.example.myp.FireBase.models;

import android.os.Parcel;
import android.os.Parcelable;

// FireBase Class to add Parents and Teachers to a ClassRoom
public class ClassRoomMember implements Parcelable{

    private String user_id;
    private String role;
    private String member_status;
    private String member_full_name;

    private ClassRoomMember(String user_id, String role, String member_status, String member_full_name){
        this.user_id = user_id;
        this.role = role;
        this.member_status = member_status;
        this.member_full_name = member_full_name;
    }
    public ClassRoomMember(){

    }

    protected ClassRoomMember (Parcel in){
        user_id = in.readString();
        role = in.readString();
        member_status = in.readString();
        member_full_name = in.readString();
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

    public String getMember_full_name(){return member_full_name;}
    public void setMember_full_name(String member_full_name){this.member_full_name = member_full_name;}

    @Override
    public String toString(){
        return "classroomMembers{" +
                "userID='" + user_id + '\'' +
                ",member_role='" + role + '\'' +
                ",member_status='" + member_status + '\'' +
                ",member_full_name='" + member_full_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(role);
        dest.writeString(member_status);
        dest.writeString(member_full_name);
    }
}