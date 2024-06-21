package com.example.myapp_keshe.Control;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    MyHelper myHelper;
    private EditText editTextuserName;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        myHelper = new MyHelper(this);
        init();
    }

    private void init() {
        // 初始化
        editTextuserName = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        Button loginbutton = findViewById(R.id.loginbutton);
        Button registerbutton = findViewById(R.id.registerbutton);

        loginbutton.setOnClickListener(this);
        registerbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username, password;
        SQLiteDatabase db;

        if (v.getId() == R.id.loginbutton) {
            // 获取用户输入的用户名和密码
            username = editTextuserName.getText().toString();
            password = editTextPassword.getText().toString();
            // 检查是否输入了用户名和密码
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }
            db = myHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    "users",
                    new String[]{"name", "password"},
                    "name=? AND password=?",  // WHERE 子句
                    new String[]{username, password},
                    null,
                    null,
                    null
            );
            // 检查查询结果
            if (cursor.moveToFirst()) {
                // 登录成功
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                // 跳转到新的页面
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 登录失败
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
            // 关闭游标和数据库连接
            cursor.close();
            db.close();
        } else if (v.getId() == R.id.registerbutton) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}