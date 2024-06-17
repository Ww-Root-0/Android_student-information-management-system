package com.example.myapp_keshe.Control;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.Adapter.ScoreAdapter;
import com.example.myapp_keshe.Pojo.Score;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {

    // 定义UI组件
    private EditText editTextStuId, editTextStuName, editTextCourseId, editTextCourseName, editTextScore;
    private ListView listViewResults;
    private MyHelper myHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        myHelper = new MyHelper(this);
        init();
    }

    private void init() {
        editTextStuId = findViewById(R.id.editTextStuId);
        editTextStuName = findViewById(R.id.editTextStuName);
        editTextCourseId = findViewById(R.id.editTextCourseId);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextScore = findViewById(R.id.editTextScore);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // 显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 设置返回按钮的点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonUpdate).setOnClickListener(this);
        findViewById(R.id.buttonDelete).setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);

        listViewResults = findViewById(R.id.listViewResults);
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String grade_id = cursor.getString(cursor.getColumnIndexOrThrow("grade_id"));
                String student_id = cursor.getString(cursor.getColumnIndexOrThrow("student_id"));
                String course_id = cursor.getString(cursor.getColumnIndexOrThrow("course_id"));
                String score = cursor.getString(cursor.getColumnIndexOrThrow("score"));

                editTextStuId.setText(student_id);
                editTextCourseId.setText(course_id);
                editTextScore.setText(score);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonAdd) {
            addScore();
        } else if (id == R.id.buttonUpdate) {
            updateScore();
        } else if (id == R.id.buttonDelete) {
            deleteScore();
        } else if (id == R.id.buttonSearch) {
            searchScores();
        }
    }

    private void addScore() {
        String studentId = editTextStuId.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();
        String score = editTextScore.getText().toString().trim();

        if (studentId.isEmpty() || courseId.isEmpty() || score.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("course_id", courseId);
        values.put("score", score);

        long newRowId = db.insert("grades", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        String studentId = editTextStuId.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();
        String score = editTextScore.getText().toString().trim();

        if (studentId.isEmpty() || courseId.isEmpty() || score.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);

        int rowsAffected = db.update("grades", values, "student_id=? AND course_id=?", new String[]{studentId, courseId});
        if (rowsAffected > 0) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteScore() {
        String studentId = editTextStuId.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();

        if (studentId.isEmpty() || courseId.isEmpty()) {
            Toast.makeText(this, "请填写学号和课程代号", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        int rowsAffected = db.delete("grades", "student_id=? AND course_id=?", new String[]{studentId, courseId});
        if (rowsAffected > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchScores() {
        String studentId = editTextStuId.getText().toString().trim();
        String studentName = editTextStuName.getText().toString().trim();
        String courseId = editTextCourseId.getText().toString().trim();
        String courseName = editTextCourseName.getText().toString().trim();

        SQLiteDatabase db = myHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!studentId.isEmpty()) {
            selection.append("student_id LIKE ?");
            selectionArgs.add("%" + studentId + "%");
        }
        if (!studentName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("student_id IN (SELECT student_id FROM users WHERE name LIKE ?)");
            selectionArgs.add("%" + studentName + "%");
        }
        if (!courseId.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_id LIKE ?");
            selectionArgs.add("%" + courseId + "%");
        }
        if (!courseName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_id IN (SELECT course_id FROM courses WHERE course_name LIKE ?)");
            selectionArgs.add("%" + courseName + "%");
        }

        Cursor cursor = db.query(
                "grades", // 表名
                new String[]{"grade_id AS _id", "student_id", "course_id", "score"}, // 返回的列
                selection.toString(), // WHERE 子句
                selectionArgs.toArray(new String[0]), // WHERE 子句中的占位符的值
                null, // GROUP BY 子句
                null, // HAVING 子句
                null // ORDER BY 子句
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的成绩", Toast.LENGTH_SHORT).show();
        } else {
            List<Score> scoreList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String score = cursor.getString(cursor.getColumnIndex("score"));
                String studentIdFromCursor = cursor.getString(cursor.getColumnIndex("student_id"));
                String courseIdFromCursor = cursor.getString(cursor.getColumnIndex("course_id"));

                Cursor cursor1 = db.query(
                        "users", // 表名
                        new String[]{"name"}, // 返回的列
                        "_id=?", // WHERE 子句
                        new String[]{studentIdFromCursor}, // WHERE 子句中的占位符的值
                        null, // GROUP BY 子句
                        null, // HAVING 子句
                        null // ORDER BY 子句
                );
                Cursor cursor2 = db.query(
                        "courses", // 表名
                        new String[]{"course_name"}, // 返回的列
                        "course_id=?", // WHERE 子句
                        new String[]{courseIdFromCursor}, // WHERE 子句中的占位符的值
                        null, // GROUP BY 子句
                        null, // HAVING 子句
                        null // ORDER BY 子句
                );

                if (cursor1.moveToFirst() && cursor2.moveToFirst()) {
                    String studentNameFromCursor = cursor1.getString(cursor1.getColumnIndex("name"));
                    String courseNameFromCursor = cursor2.getString(cursor2.getColumnIndex("course_name"));
                    scoreList.add(new Score(score, studentNameFromCursor, courseNameFromCursor));
                }

                cursor1.close();
                cursor2.close();
            }
            cursor.close();

            ScoreAdapter adapter = new ScoreAdapter(this, scoreList);
            listViewResults.setAdapter(adapter);
        }
    }    // 清除输入框内容
    private void clearInputs() {
        editTextStuId.setText("");
        editTextStuName.setText("");
        editTextCourseId.setText("");
        editTextCourseName.setText("");
        editTextScore.setText("");
    }
}