package com.hiroshi.courseschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiroshi.courseschedule.db.SqlTest;
import com.hiroshi.courseschedule.entity.Login;

import java.util.List;
/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText et_name, et_tel, et_pass, et_pass2;
    private Button btn_regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String tel = et_tel.getText().toString();
                String pass1 = et_pass.getText().toString();
                String pass2 = et_pass2.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "用户名输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tel.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "手机号输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tel.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "手机号输入格式不对！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass1.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "密码输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass2.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "确认密码输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass1.equals(pass2)) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一样。", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Login> select = SqlTest.select(RegisterActivity.this);
                for (int i = 0; i < select.size(); i++) {
                    String mName = select.get(i).getName();
                    if (name.equals(mName)) {
                        Toast.makeText(RegisterActivity.this, "该用户已注册过。", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, "如果忘记密码，请找回密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                SqlTest.insert(RegisterActivity.this, new Login(name, tel, pass1));
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_tel = findViewById(R.id.et_tel);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        btn_regis = findViewById(R.id.btn_regis);
    }
}