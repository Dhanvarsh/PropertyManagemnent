package com.propertymanagment.database.model;

public class UserDetailModel {
    public static final String TABLE_NAME="users_details";
    public static final String USER_ID = "id";
    public static final String DETAIL = "user_detail";

    private int id;
    private String user_detail;


    //CREATE TABLE
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +DETAIL+" TEXT "
            +")";
    public UserDetailModel(){

    }
    public UserDetailModel(int id,String user_detail){
        this.id=id;
        this.user_detail=user_detail;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(String user_detail) {
        this.user_detail = user_detail;
    }
}
