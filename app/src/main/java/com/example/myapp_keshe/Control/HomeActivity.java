package com.example.myapp_keshe.Control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        init();
    }

    private void init() {
        // 初始化按钮并设置点击监听器
        findViewById(R.id.btnClassQuery).setOnClickListener(this);
        findViewById(R.id.btnStudentInfoQuery).setOnClickListener(this);
        findViewById(R.id.btnGradeQuery).setOnClickListener(this);
        findViewById(R.id.btnUserManagement).setOnClickListener(this);
        findViewById(R.id.btnStudentManagement).setOnClickListener(this);
        findViewById(R.id.btnCourseManagement).setOnClickListener(this);
        findViewById(R.id.btnGradeManagement).setOnClickListener(this);
        findViewById(R.id.btnClassManagement).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnClassQuery) {
            // 跳转到班级查询界面
            startActivity(new Intent(this, ClassQueryActivity.class));
        } else if (id == R.id.btnStudentInfoQuery) {
            // 跳转到学生信息查询界面
            startActivity(new Intent(this, StudentInfoQueryActivity.class));
        } else if (id == R.id.btnGradeQuery) {
            // 跳转到成绩查询界面
            startActivity(new Intent(this, GradeQueryActivity.class));
        } else if (id == R.id.btnUserManagement) {
            // 跳转到用户管理界面
            startActivity(new Intent(this, UserManagementActivity.class));
        } else if (id == R.id.btnStudentManagement) {
            // 跳转到学生管理界面
            startActivity(new Intent(this, StudentManagementActivity.class));
        } else if (id == R.id.btnCourseManagement) {
            // 跳转到课程管理界面
            startActivity(new Intent(this, CourseManagementActivity.class));
        } else if (id == R.id.btnGradeManagement) {
            // 跳转到成绩管理界面
            startActivity(new Intent(this, GradeManagementActivity.class));
        } else if (id == R.id.btnClassManagement) {
            // 跳转到班级管理界面
            startActivity(new Intent(this, ClassManagementActivity.class));
        } else if (id == R.id.btnExit) {
            // 退出系统
            finish();
        }
    }
}