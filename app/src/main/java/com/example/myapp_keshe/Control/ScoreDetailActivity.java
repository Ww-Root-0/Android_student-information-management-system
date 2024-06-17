package com.example.myapp_keshe.Control;

import android.content.ContentValues;
import android.database.Cursor;
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

public class ScoreDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextStudentName, editTextCourseName, editTextScore;
    private Button buttonUpdate, buttonOK;
    private MyHelper myHelper;
    private Toolbar toolbar;
    private String originalStudentId, originalCourseId; // 用于存储原始学生ID和课程ID，作为更新的条件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_xx);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // 显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // 设置返回按钮的点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        myHelper = new MyHelper(this);

        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextScore = findViewById(R.id.editTextScore);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonOK = findViewById(R.id.buttonOK);

        // 设置按钮点击事件监听器
        buttonUpdate.setOnClickListener(this);
        buttonOK.setOnClickListener(this);

        // 获取传递的数据
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String studentName = extras.getString("studentName");
            String courseName = extras.getString("courseName");
            String score = extras.getString("score");

            // 获取学生ID和课程ID
            originalStudentId = getStudentIdByName(studentName);
            originalCourseId = getCourseIdByName(courseName);

            // 设置EditText的内容
            editTextStudentName.setText(studentName);
            editTextCourseName.setText(courseName);
            editTextScore.setText(score);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 返回上一个活动
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonUpdate) {
            // 将EditText设置为可编辑状态
            setEditTextsEnabled(true);
        } else if (v.getId() == R.id.buttonOK) {
            // 获取EditText中的新数据
            String newStudentName = editTextStudentName.getText().toString();
            String newCourseName = editTextCourseName.getText().toString();
            String newScore = editTextScore.getText().toString();

            // 获取新的学生ID和课程ID
            String newStudentId = getStudentIdByName(newStudentName);
            String newCourseId = getCourseIdByName(newCourseName);

            // 执行更新操作
            updateScoreData(originalStudentId, originalCourseId, newStudentId, newCourseId, newScore);

            // 将EditText设置为不可编辑状态
            setEditTextsEnabled(false);

            // 提示用户修改成功
            Toast.makeText(this, "成绩信息修改成功", Toast.LENGTH_SHORT).show();
        }
    }

    // 设置EditText的可编辑状态
    private void setEditTextsEnabled(boolean enabled) {
        editTextStudentName.setEnabled(enabled);
        editTextCourseName.setEnabled(enabled);
        editTextScore.setEnabled(enabled);
    }

    // 更新成绩数据
    private void updateScoreData(String originalStudentId, String originalCourseId, String newStudentId, String newCourseId, String newScore) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", newStudentId);
        values.put("course_id", newCourseId);
        values.put("score", newScore);

        // 更新数据库中的成绩信息
        String selection = "student_id = ? AND course_id = ?";
        String[] selectionArgs = { originalStudentId, originalCourseId };
        db.update("grades", values, selection, selectionArgs);
        db.close();
    }

    // 根据学生姓名获取学生ID
    private String getStudentIdByName(String studentName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"_id"}, "name = ?", new String[]{studentName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String studentId = cursor.getString(cursor.getColumnIndex("_id"));
            cursor.close();
            return studentId;
        }
        return null;
    }

    // 根据课程名获取课程ID
    private String getCourseIdByName(String courseName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("courses", new String[]{"course_id"}, "course_name = ?", new String[]{courseName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String courseId = cursor.getString(cursor.getColumnIndex("course_id"));
            cursor.close();
            return courseId;
        }
        return null;
    }
}