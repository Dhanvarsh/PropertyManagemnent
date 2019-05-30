package com.propertymanagment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.propertymanagment.database.model.UserDetailModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Product_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create Table
        db.execSQL(UserDetailModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + UserDetailModel.TABLE_NAME);

        //Create Table Again
        onCreate(db);
    }

    //insert new row
    public long insertProduct(String user_detail) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDetailModel.DETAIL, user_detail);

        // insert row
        long id = db.insert(UserDetailModel.TABLE_NAME, null, contentValues);
        // return newly inserted row id
        return id;

    }

    public UserDetailModel getUserDetail(long user_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserDetailModel.TABLE_NAME,
                new String[]{UserDetailModel.USER_ID, UserDetailModel.DETAIL},
                UserDetailModel.USER_ID + "=?",
                new String[]{String.valueOf(user_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        UserDetailModel userDetailsModel = new UserDetailModel(
                cursor.getInt(cursor.getColumnIndex(UserDetailModel.USER_ID)),
                cursor.getString(cursor.getColumnIndex(UserDetailModel.DETAIL))
                // cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY))
        );

        // close the db connection
        cursor.close();

        return userDetailsModel;
    }

    public ArrayList<UserDetailModel> getAllUsersDetails() {
        ArrayList<UserDetailModel> userDetailsModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + UserDetailModel.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                if (cursor.getCount() > 0) {

                    UserDetailModel userDetailsModel = new UserDetailModel();
                    userDetailsModel.setId(cursor.getInt(cursor.getColumnIndex(UserDetailModel.USER_ID)));
                    userDetailsModel.setUser_detail(cursor.getString(cursor.getColumnIndex(UserDetailModel.DETAIL)));
                    //productDetailsModel.setProduct_quantity(cursor.getInt(cursor.getColumnIndex(ProductDetailsModel.PRODUCT_QUANTITY)));
                    userDetailsModels.add(userDetailsModel);
                } else {
                    System.out.println("In ELSE Condtion");
                }

            } while (cursor.moveToNext());
        }
        db.close();
        return userDetailsModels;

    }

    public int updateProduct(String user_detail, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDetailModel.DETAIL, user_detail);

        // updating row
        return db.update(UserDetailModel.TABLE_NAME, values, UserDetailModel.USER_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }
}
