package com.hiroshi.courseschedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LoginSql extends SQLiteOpenHelper {

    public LoginSql(@Nullable Context context) {
        super(context, "User", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Login(" +
                "name varchar(20)," +
                "tel varchar(20)," +
                "pass varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
