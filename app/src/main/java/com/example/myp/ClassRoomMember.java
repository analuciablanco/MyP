package com.example.myp;

import android.os.Parcel;
import android.os.Parcelable;

// FireBase Class to add Parents and Teachers to a ClassRoom
public class ClassRoomMember implements Parcelable{

    private String member_id;
    private String user_id;
    private String role;

    private ClassRoomMember(String member_id,String user_id, String role){
        this.member_id = member_id;
        this.user_id = user_id;
        this.role = role;
    }
    public ClassRoomMember(){

    }

    protected ClassRoomMember (Parcel in){
        member_id = in.readString();
        user_id = in.readString();
        role = in.readString();
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

    @Override
    public String toString(){
        return "classroomMembers{" +
                "memberID='" + member_id + '\'' +
                ",userID='" + user_id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(member_id);
        dest.writeString(user_id);
        dest.writeString(role);
    }
}
