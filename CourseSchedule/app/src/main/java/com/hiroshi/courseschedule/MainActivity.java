package com.hiroshi.courseschedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hiroshi.courseschedule.entity.Course;
import com.hiroshi.courseschedule.db.DatabaseHelper;
import com.hiroshi.courseschedule.service.MyService;
import com.hiroshi.courseschedule.util.Util;

import java.util.ArrayList;

/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 * 首页
 */
public class MainActivity extends AppCompatActivity {
    //星期几
    private RelativeLayout day;
    int color[] = {
            Color.parseColor("#D4B477"),
            Color.parseColor("#97AFC9"),
            Color.parseColor("#A190C7"),
            Color.parseColor("#CEA498"),
            Color.parseColor("#5FAFD4"),
            Color.parseColor("#DCC091"),
            Color.parseColor("#9ECB66"),
            Color.parseColor("#F0C370"),
            Color.parseColor("#D88EA5"),
            Color.parseColor("#83C5C3"),
            Color.parseColor("#EFA278"),
    };
    int id = 0;
    private Switch swBtServer;

    //SQLite Helper类
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //创建课程表左边视图(节数)
        createLeftView();

        //从数据库读取数据
        loadData();
    }

    //从数据库加载数据
    @SuppressLint("Range")
    private void loadData() {
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")), cursor.getString(cursor.getColumnIndex("class_room")), cursor.getInt(cursor.getColumnIndex("day")), cursor.getInt(cursor.getColumnIndex("class_start")), cursor.getInt(cursor.getColumnIndex("class_end"))));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createItemCourseView(course);
        }
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)", new String[]{course.getCourseName(), course.getTeacher(), course.getClassRoom(), course.getDay() + "", course.getStart() + "", course.getEnd() + ""});
    }

    //创建"第几节数"视图
    private void createLeftView() {
        for (int i = 1; i < 13; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110, 180);
            view.setLayoutParams(params);

            TextView text = view.findViewById(R.id.class_number_text);
            text.setText(String.valueOf(i));

            LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
            leftViewLayout.addView(view);
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1:
                    dayId = R.id.monday;
                    break;
                case 2:
                    dayId = R.id.tuesday;
                    break;
                case 3:
                    dayId = R.id.wednesday;
                    break;
                case 4:
                    dayId = R.id.thursday;
                    break;
                case 5:
                    dayId = R.id.friday;
                    break;
            }
            day = findViewById(dayId);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
            v.setY(height * (course.getStart() - 1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (course.getEnd() - course.getStart() + 1) * height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            CardView cardView = v.findViewById(R.id.cv);
            cardView.setCardBackgroundColor(color[id++ % 11]);
            text.setText(course.getCourseName() + "\n" + "\n" +
                    course.getClassRoom()); //显示课程名
            day.addView(v);
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("是否确定删除该课程！");
                    //添加确定按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            v.setVisibility(View.GONE);//先隐藏
                            day.removeView(v);//再移除课程视图
                            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                            sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[]{course.getCourseName()});
                        }
                    });
                    //添加取消按钮
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }


            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switch);
        swBtServer = item.getActionView().findViewById(R.id.server_switch);
        swBtServer.setChecked(Util.loadTong(MainActivity.this));
        swBtServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Util.saveTong(MainActivity.this, true);
                    Intent intent2 = new Intent(MainActivity.this, MyService.class);
                    startService(intent2); // 启动Service
                    Toast.makeText(MainActivity.this, "开启课程提醒。", Toast.LENGTH_SHORT).show();
                } else {
                    Util.saveTong(MainActivity.this, true);
                    Intent intent2 = new Intent(MainActivity.this, MyService.class);
                    stopService(intent2); // 停止Service
                    Toast.makeText(MainActivity.this, "关闭课程提醒。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_courses) {
            Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
            startActivityForResult(intent, 0);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            saveData(course);
        }
    }
}