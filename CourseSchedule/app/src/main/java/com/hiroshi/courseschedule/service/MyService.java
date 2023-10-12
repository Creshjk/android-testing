package com.hiroshi.courseschedule.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hiroshi.courseschedule.MainActivity;
import com.hiroshi.courseschedule.R;
import com.hiroshi.courseschedule.entity.Course;
import com.hiroshi.courseschedule.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//上课通知qqqqq
public class MyService extends Service {
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                while (true) {
                    try {

                        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
                        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
                        if (cursor.moveToFirst()) {
                            do {
                                coursesList.add(new Course(
                                        cursor.getString(cursor.getColumnIndex("course_name")),
                                        cursor.getString(cursor.getColumnIndex("teacher")),
                                        cursor.getString(cursor.getColumnIndex("class_room")),
                                        cursor.getInt(cursor.getColumnIndex("day")),
                                        cursor.getInt(cursor.getColumnIndex("class_start")),
                                        cursor.getInt(cursor.getColumnIndex("class_end"))));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();

//                        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                        Calendar cal = Calendar.getInstance();

                        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;//获取当天是周几
                        Log.i("MyService", "---------------------week=" + week);

                        List<Course> courses = new ArrayList<>();
                        Log.i("MyService", "---------------------coursesList.size()=" + coursesList.size());
                        for (int i = 0; i < coursesList.size(); i++) {
                            if (coursesList.get(i).getDay() == week) {
                                courses.add(coursesList.get(i));

                            }
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String str = sdf.format(new Date());
                        Log.i("MyService", "---------------------time=" + str);

                        Log.i("MyService", "---------------------courses.size()=" + courses.size());
                        for (int i = 0; i < courses.size(); i++) {
                            Log.i("MyService", "---------------------" + courses.get(i).getCourseName());
                            Log.i("MyService", "---------------------" + courses.get(i).getStart());
                            times(str, courses.get(i).getStart(), courses.get(i).getCourseName() + "快开始了！");
                        }


                        Thread.sleep(1000 * 60 * 10);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

    private void times(String str, int m, String name) {
        Log.i("MyService", "---------------------m=" + m);
        String[] split = str.split(":");
        Log.i("MyService", "---------------------split[0]=" + Integer.parseInt(split[0]));
        Log.i("MyService", "---------------------split[1]=" + Integer.parseInt(split[1]));
        if (m == 1) {//8:00
            if (Integer.parseInt(split[0]) == 7) {
                if (Integer.parseInt(split[1]) < 59 && Integer.parseInt(split[1]) > 49) {
                    Log.i("MyService", "---------------------" + name);
                    sendSimpleNotify(name);
                }
            }
        } else if (m == 3) {
            if (Integer.parseInt(split[0]) == 8) {
                if (Integer.parseInt(split[1]) < 59 && Integer.parseInt(split[1]) > 49) {
                    Log.i("MyService", "---------------------" + name);
                    sendSimpleNotify(name);
                }
            }
        } else if (m == 6) {
            if (Integer.parseInt(split[0]) == 13) {
                if (Integer.parseInt(split[1]) < 39 && Integer.parseInt(split[1]) > 29) {
                    Log.i("MyService", "---------------------" + name);
                    sendSimpleNotify(name);
                }
            }
        } else if (m == 10) {
            if (Integer.parseInt(split[0]) == 18) {
                if (Integer.parseInt(split[1]) < 29 && Integer.parseInt(split[1]) > 19) {
                    Log.i("MyService", "---------------------" + name);
                    sendSimpleNotify(name);
                }
            }
        }
    }


    // 发送简单的通知消息（包括消息标题和消息内容）
    private void sendSimpleNotify(String message) {
        String title = "上课提醒";
        // 发送消息之前要先创建通知渠道，创建代码见MainApplication.java
        // 创建一个跳转到活动页面的意图
        Intent clickIntent = new Intent(this, MainActivity.class);
//        // 创建一个用于页面跳转的延迟意图
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.string.app_name, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 创建一个通知消息的建造器
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            builder = new Notification.Builder(this, getString(R.string.app_name));
        }
        builder
//                .setContentIntent(contentIntent) // 设置内容的点击意图
                .setAutoCancel(true) // 点击通知栏后是否自动清除该通知
                .setSmallIcon(R.mipmap.ic_launcher) // 设置应用名称左边的小图标
                .setSubText("这里是副本") // 设置通知栏里面的附加说明文本
                .setContentTitle(title) // 设置通知栏里面的标题文本
                .setContentText(message); // 设置通知栏里面的内容文本
        Notification notify = builder.build(); // 根据通知建造器构建一个通知对象
        // 从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        // 使用通知管理器推送通知，然后在手机的通知栏就会看到该消息
        notifyMgr.notify(R.string.app_name, notify);
    }


}
