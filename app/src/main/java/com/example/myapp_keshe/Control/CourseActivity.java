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

import com.example.myapp_keshe.Date.CourseAdapter;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    // 定义UI组件
    private EditText editTextCourseName, editTextCourseCode, editTextInstructor, editTextCredits;
    private Button buttonAdd, buttonUpdate, buttonDelete, buttonSearch, buttonBack;
    private ListView listViewResults;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.course);

        // 初始化数据库助手
        myHelper = new MyHelper(this);

        // 初始化UI组件
        init();
    }

    // 初始化UI组件和设置事件监听器
    private void init() {
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextCourseCode = findViewById(R.id.editTextCourseCode);
        editTextInstructor = findViewById(R.id.editTextInstructor);
        editTextCredits = findViewById(R.id.editTextCredits);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonBack = findViewById(R.id.buttonBack);
        listViewResults = findViewById(R.id.listViewResults);

        // 设置返回按钮的点击事件监听器
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置添加按钮的点击事件监听器
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        // 设置更新按钮的点击事件监听器
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

        // 设置删除按钮的点击事件监听器
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });

        // 设置查询按钮的点击事件监听器
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
                String courseId = cursor.getString(cursor.getColumnIndexOrThrow("course_id"));
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
        String courseName = editTextCourseName.getText().toString().trim();
        String courseCode = editTextCourseCode.getText().toString().trim();
        String instructor = editTextInstructor.getText().toString().trim();
        String creditsStr = editTextCredits.getText().toString().trim();

        Integer credits = null;
        if (!creditsStr.isEmpty()) {
            try {
                credits = Integer.parseInt(creditsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "学分必须是一个有效的数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("INSERT INTO courses (course_name, course_code, instructor, credits) VALUES (?, ?, ?, ?)",
                new Object[]{courseName.isEmpty() ? null : courseName, courseCode.isEmpty() ? null : courseCode, instructor.isEmpty() ? null : instructor, credits});
        Toast.makeText(this, "课程信息添加成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 更新课程信息
    private void updateCourse() {
        String courseName = editTextCourseName.getText().toString().trim();
        String courseCode = editTextCourseCode.getText().toString().trim();
        String instructor = editTextInstructor.getText().toString().trim();
        String creditsStr = editTextCredits.getText().toString().trim();

        Integer credits = null;
        if (!creditsStr.isEmpty()) {
            try {
                credits = Integer.parseInt(creditsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "学分必须是一个有效的数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("UPDATE courses SET course_code = ?, instructor = ?, credits = ? WHERE course_name = ?",
                new Object[]{courseCode.isEmpty() ? null : courseCode, instructor.isEmpty() ? null : instructor, credits, courseName.isEmpty() ? null : courseName});
        Toast.makeText(this, "课程信息更新成功", Toast.LENGTH_SHORT).show();
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
// 查询课程信息
// 查询课程信息
    private void searchCourses() {
        String courseName = editTextCourseName.getText().toString().trim();
        String courseCode = editTextCourseCode.getText().toString().trim();
        String instructor = editTextInstructor.getText().toString().trim();
        String creditsStr = editTextCredits.getText().toString().trim();

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
                new String[]{"course_id AS _id", "course_name", "course_code", "instructor", "credits"}, // 返回的列
                selection.toString(), // WHERE 子句
                selectionArgs.toArray(new String[0]), // WHERE 子句中的占位符的值
                null, // GROUP BY 子句
                null, // HAVING 子句
                null // ORDER BY 子句
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的信息", Toast.LENGTH_SHORT).show();
        } else {
            // 使用自定义的CourseAdapter来显示数据
            CourseAdapter adapter = new CourseAdapter(this, cursor);
            listViewResults.setAdapter(adapter);
        }
    }    // 清除输入框内容
    private void clearInputs() {
        editTextCourseName.setText("");
        editTextCourseCode.setText("");
        editTextInstructor.setText("");
        editTextCredits.setText("");
    }
}