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
        findViewById(R.id.btnQuery).setOnClickListener(this);
        findViewById(R.id.btnStudent).setOnClickListener(this);
        findViewById(R.id.btnCourse).setOnClickListener(this);
        findViewById(R.id.btnScore).setOnClickListener(this);
        findViewById(R.id.btnClass).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnQuery) {
            // 跳转到信息查询界面
            startActivity(new Intent(this, SearchActivity.class));
        } else if (id == R.id.btnStudent) {
            // 跳转到学生管理界面
            startActivity(new Intent(this, StudentActivity.class));
        } else if (id == R.id.btnCourse) {
            // 跳转到课程管理界面
            startActivity(new Intent(this, CourseManagementActivity.class));
        } else if (id == R.id.btnScore) {
            // 跳转到成绩管理界面
            startActivity(new Intent(this, GradeManagementActivity.class));
        } else if (id == R.id.btnClass) {
            // 跳转到班级管理界面
            startActivity(new Intent(this, ClassManagementActivity.class));
        } else if (id == R.id.btnExit) {
            // 退出系统
            finish();
        }
    }
}