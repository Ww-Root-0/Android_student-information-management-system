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

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextCourseId, editTextCourseName, editTextCourseCode, editTextInstructor, editTextCredits;
    private Button buttonUpdate, buttonOK;
    private MyHelper myHelper;
    private Toolbar toolbar;
    private String originalCourseId; // 用于存储原始课程ID，作为更新的条件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_xx);

        myHelper = new MyHelper(this);

        editTextCourseId = findViewById(R.id.editTextCourseId);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextCourseCode = findViewById(R.id.editTextCourseCode);
        editTextInstructor = findViewById(R.id.editTextInstructor);
        editTextCredits = findViewById(R.id.editTextCredits);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonOK = findViewById(R.id.buttonOK);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // 显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // 设置返回按钮的点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 设置按钮点击事件监听器
        buttonUpdate.setOnClickListener(this);
        buttonOK.setOnClickListener(this);

        // 获取传递的数据
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String courseId = extras.getString("courseId");
            originalCourseId = courseId; // 存储原始课程ID
            String courseName = extras.getString("courseName");
            String courseCode = extras.getString("courseCode");
            String instructor = extras.getString("instructor");
            int credits = extras.getInt("credits");

            // 设置EditText的内容
            editTextCourseId.setText(courseId);
            editTextCourseName.setText(courseName);
            editTextCourseCode.setText(courseCode);
            editTextInstructor.setText(instructor);
            editTextCredits.setText(String.valueOf(credits));
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
            String newCourseId = editTextCourseId.getText().toString();
            String newCourseName = editTextCourseName.getText().toString();
            String newCourseCode = editTextCourseCode.getText().toString();
            String newInstructor = editTextInstructor.getText().toString();
            int newCredits = Integer.parseInt(editTextCredits.getText().toString());

            // 执行更新操作
            updateCourseData(originalCourseId, newCourseId, newCourseName, newCourseCode, newInstructor, newCredits);

            // 将EditText设置为不可编辑状态
            setEditTextsEnabled(false);

            // 提示用户修改成功
            Toast.makeText(this, "课程信息修改成功", Toast.LENGTH_SHORT).show();
        }
    }

    // 设置EditText的可编辑状态
    private void setEditTextsEnabled(boolean enabled) {
        editTextCourseId.setEnabled(enabled);
        editTextCourseName.setEnabled(enabled);
        editTextCourseCode.setEnabled(enabled);
        editTextInstructor.setEnabled(enabled);
        editTextCredits.setEnabled(enabled);
    }

    // 更新课程数据
    private void updateCourseData(String originalCourseId, String newCourseId, String newCourseName, String newCourseCode, String newInstructor, int newCredits) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course_id", newCourseId);
        values.put("course_name", newCourseName);
        values.put("course_code", newCourseCode);
        values.put("instructor", newInstructor);
        values.put("credits", newCredits);

        // 更新数据库中的课程信息
        String selection = "course_id = ?";
        String[] selectionArgs = { originalCourseId };
        db.update("courses", values, selection, selectionArgs);
        db.close();
    }
}