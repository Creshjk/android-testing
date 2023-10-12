package com.hiroshi.courseschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiroshi.courseschedule.entity.Course;
import com.hiroshi.courseschedule.db.DatabaseHelper;
import com.hiroshi.courseschedule.entity.Data02;
import com.hiroshi.courseschedule.util.Util;

import java.util.ArrayList;
import java.util.List;
//显示开始界面
/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 *
 */
public class SplashActivity extends AppCompatActivity {

    private TextView tv_time;
    private ImageView iv;

    private int time = 3;
    private AppCompatButton btn_start;
    private Thread mThread;
    private Boolean isTrue = true;

    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int time = (int) msg.obj;
                tv_time.setText(time + "s");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_time = findViewById(R.id.tv_time);
        iv = findViewById(R.id.iv);
        btn_start = findViewById(R.id.btn_start);

        if (!Util.loadSetting(this)) {
            List<Course> list = new ArrayList<>();
            for (int i = 0; i < 13; i++) {
                list.add(new Course(Data02.course[i], Data02.teacher[i],
                        Data02.classRoom[i], Data02.day[i], Data02.start[i], Data02.end[i]));
            }

            for (int i = 0; i < 13; i++) {
                Course course = list.get(i);
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                sqLiteDatabase.execSQL("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
                        new String[]{course.getCourseName(), course.getTeacher(), course.getClassRoom(), course.getDay() + "", course.getStart() + "", course.getEnd() + ""});
            }
        }

        //创建子线程
        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (isTrue) {
                    try {
                        sleep(1300);//使程序休眠3秒
                        time--;
                        Log.i("AGT", "----------------" + time);
                        Message msg = handler.obtainMessage();
                        msg.obj = time;
                        msg.what = 1;
                        handler.sendMessage(msg);
                        if (time == 0) {
                            isTrue = false;
                            Util.saveSetting(SplashActivity.this, true);
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();//启动线程

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.saveSetting(SplashActivity.this, true);
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTrue = false;
        mThread.interrupt();
        mThread = null;
    }
}