package com.example.myapp_keshe.Control;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapp_keshe.Adapter.CourseAdapter;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    // 定义UI组件
    private EditText editTextCourseName, editTextCourseCode, editTextInstructor, editTextCredits;
    private Button buttonAdd, buttonUpdate, buttonDelete, buttonSearch;
    private ListView listViewResults;
    private MyHelper myHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        myHelper = new MyHelper(this);
        init();
    }

    // 初始化
    private void init() {
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextCourseCode = findViewById(R.id.editTextCourseCode);
        editTextInstructor = findViewById(R.id.editTextInstructor);
        editTextCredits = findViewById(R.id.editTextCredits);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewResults = findViewById(R.id.listViewResults);
        toolbar=findViewById(R.id.toolbar);
        //返回
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 设置添加事件监听器
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
        // 设置更新事件监听器
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

        // 设置删除事件监听器
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });

        // 设置查询事件监听器
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCourses();
            }
        });

        // 设置ListView的点击事件监听器
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow("course_name"));
                String courseCode = cursor.getString(cursor.getColumnIndexOrThrow("course_code"));
                String instructor = cursor.getString(cursor.getColumnIndexOrThrow("instructor"));
                int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));

                editTextCourseName.setText(courseName);
                editTextCourseCode.setText(courseCode);
                editTextInstructor.setText(instructor);
                editTextCredits.setText(String.valueOf(credits));
            }
        });
    }

    // 添加课程信息
    private void addCourse() {
        String courseName = editTextCourseName.getText().toString();
        String courseCode = editTextCourseCode.getText().toString();
        String instructor = editTextInstructor.getText().toString();
        String creditsStr = editTextCredits.getText().toString();

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("INSERT INTO courses (course_name, course_code, instructor, credits) VALUES (?, ?, ?, ?)",
                new Object[]{courseName.isEmpty() ? null : courseName,
                        courseCode.isEmpty() ? null : courseCode,
                        instructor.isEmpty() ? null : instructor,
                        creditsStr});
        Toast.makeText(this, "课程信息添加成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 更新课程信息
    private void updateCourse() {
        String courseName = editTextCourseName.getText().toString();
        String courseCode = editTextCourseCode.getText().toString();
        String instructor = editTextInstructor.getText().toString();
        String creditsStr = editTextCredits.getText().toString();


        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("UPDATE courses SET course_code = ?, instructor = ?, credits = ? WHERE course_name = ?",
                new Object[]{courseCode.isEmpty() ? null : courseCode, instructor.isEmpty() ? null : instructor, creditsStr, courseName.isEmpty() ? null : courseName});
        Toast.makeText(this, "课程信息更新成功", Toast.LENGTH_SHORT).show();
        searchCourses();
        clearInputs();
    }

    // 删除课程信息
    private void deleteCourse() {
        String courseName = editTextCourseName.getText().toString().trim();

        if (courseName.isEmpty()) {
            Toast.makeText(this, "请输入课程名称", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("DELETE FROM courses WHERE course_name = ?", new Object[]{courseName});
        Toast.makeText(this, "课程信息删除成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 查询课程信息
    private void searchCourses() {
        String courseName = editTextCourseName.getText().toString();
        String courseCode = editTextCourseCode.getText().toString();
        String instructor = editTextInstructor.getText().toString();
        String creditsStr = editTextCredits.getText().toString();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!courseName.isEmpty()) {
            selection.append("course_name LIKE ?");
            selectionArgs.add("%" + courseName + "%");
        }
        if (!courseCode.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_code = ?");
            selectionArgs.add(courseCode);
        }
        if (!instructor.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("instructor = ?");
            selectionArgs.add(instructor);
        }
        if (!creditsStr.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("credits = ?");
            selectionArgs.add(creditsStr);
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "courses", // 表名
                new String[]{"course_id AS _id", "course_name", "course_code", "instructor", "credits"},
                selection.toString(), // WHERE 子句
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的信息", Toast.LENGTH_SHORT).show();
        } else {
            // 自定义的CourseAdapter来显示数据
            CourseAdapter adapter = new CourseAdapter(this, cursor);
            listViewResults.setAdapter(adapter);
        }
    }
    // 清除输入框内容
    private void clearInputs() {
        editTextCourseName.setText("");
        editTextCourseCode.setText("");
        editTextInstructor.setText("");
        editTextCredits.setText("");
    }
}