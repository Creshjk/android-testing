package com.hiroshi.courseschedule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hiroshi.courseschedule.entity.Login;

import java.util.ArrayList;
import java.util.List;

public class SqlTest {

    public static void insert(Context context, Login login) {
        LoginSql sql = new LoginSql(context);
        SQLiteDatabase writer = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", login.getName());//姓名
        values.put("tel", login.getTel());//手机号
        values.put("pass", login.getPass());//密码
        writer.insert("Login", null, values);
        writer.close();
    }

    public static List<Login> select(Context context) {
        List<Login> list = new ArrayList<>();
        LoginSql sql = new LoginSql(context);
        SQLiteDatabase reader = sql.getWritableDatabase();
        Cursor cursor = reader.query("Login", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Login sign = new Login(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2));
            list.add(sign);
            if (list.size() > 20) {
                list.remove(0);
            }
        }
        reader.close();
        return list;
    }

}
