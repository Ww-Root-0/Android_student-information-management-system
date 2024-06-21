package com.example.myapp_keshe.Control;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

public class StudentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextPassword, editTextGender, editTextAge,
            editTextPhone, editTextClass, editTextDepartment;
    private Button buttonUpdate, buttonOK;
    private MyHelper myHelper;
    private Toolbar toolbar;
    private String originalName; // 用于存储原始姓名，作为更新的条件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_xx);
        myHelper = new MyHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAge = findViewById(R.id.editTextAge);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextClass = findViewById(R.id.editTextClass);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonOK = findViewById(R.id.buttonOK);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        buttonUpdate.setOnClickListener(this);
        buttonOK.setOnClickListener(this);

        // 获取传递的数据
        Bundle extras = getIntent().getExtras();
            String name = extras.getString("name");
            originalName = name; // 存储原始姓名
            String password = extras.getString("password");
            String gender = extras.getString("gender");
            int age = extras.getInt("age");
            String phone = extras.getString("phone");
            String className = extras.getString("class");
            String department = extras.getString("department");

            // 设置EditText的内容
            editTextName.setText(name);
            editTextPassword.setText(password);
            editTextGender.setText(gender);
            editTextAge.setText(String.valueOf(age));
            editTextPhone.setText(phone);
            editTextClass.setText(className);
            editTextDepartment.setText(department);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonUpdate) {
            // 将EditText设置为可编辑状态
            setEditTextsEnabled(true);
        } else if (v.getId() == R.id.buttonOK) {
            // 获取EditText中的新数据
            String newName = editTextName.getText().toString();
            String newPassword = editTextPassword.getText().toString();
            String newGender = editTextGender.getText().toString();
            int newAge = Integer.parseInt(editTextAge.getText().toString());
            String newPhone = editTextPhone.getText().toString();
            String newClass = editTextClass.getText().toString();
            String newDepartment = editTextDepartment.getText().toString();

            // 执行更新操作
            updateUserData(originalName, newName, newPassword, newGender, newAge, newPhone, newClass, newDepartment);
            // 将EditText设置为不可编辑状态
            setEditTextsEnabled(false);
            // 提示用户修改成功
            Toast.makeText(this, "信息修改成功", Toast.LENGTH_SHORT).show();
        }
    }

    // 设置EditText的可编辑状态
    private void setEditTextsEnabled(boolean enabled) {
        editTextName.setEnabled(enabled);
        editTextPassword.setEnabled(enabled);
        editTextGender.setEnabled(enabled);
        editTextAge.setEnabled(enabled);
        editTextPhone.setEnabled(enabled);
        editTextClass.setEnabled(enabled);
        editTextDepartment.setEnabled(enabled);
    }

    // 更新用户数据
    private void updateUserData(String originalName, String newName, String newPassword, String newGender, int newAge, String newPhone, String newClass, String newDepartment) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("password", newPassword);
        values.put("gender", newGender);
        values.put("age", newAge);
        values.put("phone", newPhone);
        values.put("class", newClass);
        values.put("department", newDepartment);

        // 更新数据库中的用户信息
        String selection = "name = ?";
        String[] selectionArgs = { originalName };
        db.update("users", values, selection, selectionArgs);
        db.close();
    }
}