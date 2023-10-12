package com.hiroshi.courseschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hiroshi.courseschedule.entity.Course;

//添加课程信息
/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 *
 */
public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false);

        final EditText inputCourseName = findViewById(R.id.course_name);
        final EditText inputTeacher = findViewById(R.id.teacher_name);
        final EditText inputClassRoom = findViewById(R.id.class_room);
        final EditText inputDay = findViewById(R.id.week);
        final EditText inputStart = findViewById(R.id.classes_begin);
        final EditText inputEnd = findViewById(R.id.classes_ends);

        AppCompatButton okButton = findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();

                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写,存在输入框为空！", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(day) > 7 || Integer.parseInt(day) < 1) {
                    Toast.makeText(AddCourseActivity.this, "星期几输入错误，应该在1~7之间。", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(start) >= Integer.parseInt(end)){
                    Toast.makeText(AddCourseActivity.this, "课程第几节课结束输入应该大于开始输入节数。", Toast.LENGTH_LONG).show();
                }else if (Integer.parseInt(start) < 1 || Integer.parseInt(start) > 12) {
                    Toast.makeText(AddCourseActivity.this, "课程第几节课开始输入不正确，应输入1~12之间。", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(end) < 1 || Integer.parseInt(end) > 12) {
                    Toast.makeText(AddCourseActivity.this, "课程第几节课结束输入不正确，应输入1~12之间。", Toast.LENGTH_LONG).show();
                } else {
                    Course course = new Course(courseName, teacher, classRoom,
                            Integer.parseInt(day), Integer.parseInt(start), Integer.parseInt(end));
                    Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                    intent.putExtra("course", course);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}