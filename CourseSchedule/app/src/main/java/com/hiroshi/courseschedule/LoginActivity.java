package com.hiroshi.courseschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiroshi.courseschedule.db.SqlTest;
import com.hiroshi.courseschedule.entity.Login;

import java.util.List;
/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 * 登录
 */

public class LoginActivity extends AppCompatActivity {
    private EditText et_name, et_pass;
    private Button btn_sign;
    private TextView btn_regis;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String pass = et_pass.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "密码输入框为空！", Toast.LENGTH_SHORT).show();
                    return;
                }


                List<Login> select = SqlTest.select(LoginActivity.this);
                Boolean success = true;
                for (int i = 0; i < select.size(); i++) {
                    String mName = select.get(i).getName();
                    String mPass = select.get(i).getPass();
                    String tel = select.get(i).getTel();
                    if (name.equals(mName)) {
                        success = false;
                        if (pass.equals(mPass)) {
                            Login login = new Login(name, tel, pass);

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "输入密码不正确！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (success) {
                    Toast.makeText(LoginActivity.this, "该用户未注册过，请先进行注册账户。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {

        et_name = findViewById(R.id.et_name);
        et_pass = findViewById(R.id.et_pass);
        btn_sign = findViewById(R.id.btn_sign);
        btn_regis = findViewById(R.id.btn_regis);
        cb = findViewById(R.id.cb);
    }
}