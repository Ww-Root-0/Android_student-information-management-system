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
    private EditText editTextStuName,editTextCourseName, editTextScore;
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
        editTextStuName = findViewById(R.id.editTextStuName);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextScore = findViewById(R.id.editTextScore);
        toolbar=findViewById(R.id.toolbar);
        listViewResults = findViewById(R.id.listViewResults);
        // 显示返回按钮
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonUpdate).setOnClickListener(this);
        findViewById(R.id.buttonDelete).setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);

        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Score selectedScore = (Score) parent.getItemAtPosition(position);
                    String score = selectedScore.getScore();
                    String studentName = selectedScore.getStudentName();
                    String courseName = selectedScore.getCourseName();

                    // 你可以在这里处理选中的Score对象，例如更新UI
                    editTextScore.setText(score);
                    editTextStuName.setText(studentName);
                    editTextCourseName.setText(courseName);
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
        String studentName = editTextStuName.getText().toString();
        String courseName = editTextCourseName.getText().toString();
        String score = editTextScore.getText().toString();

        if (studentName.isEmpty() || courseName.isEmpty() || score.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 根据姓名查询学生ID
        Cursor studentCursor = db.query("users", new String[]{"_id"},
                "name=?",
                new String[]{studentName},
                null,
                null,
                null);
        int studentId = -1;
        if (studentCursor.moveToFirst()) {
            studentId = studentCursor.getInt(studentCursor.getColumnIndex("_id"));
        }
        studentCursor.close();
        if (studentId == -1) {
            Toast.makeText(this, "未找到该学生", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据课程名称查询课程ID
        Cursor courseCursor = db.query("courses", new String[]{"course_id"}, "course_name=?", new String[]{courseName}, null, null, null);
        int courseId = -1;
        if (courseCursor.moveToFirst()) {
            courseId = courseCursor.getInt(courseCursor.getColumnIndex("course_id"));
        }
        courseCursor.close();
        if (courseId == -1) {
            Toast.makeText(this, "未找到该课程", Toast.LENGTH_SHORT).show();
            return;
        }

        // 插入成绩
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
        String studentName = editTextStuName.getText().toString();
        String courseName = editTextCourseName.getText().toString();
        String score = editTextScore.getText().toString();

        if (studentName.isEmpty() || courseName.isEmpty() || score.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 根据姓名查询学生ID
        Cursor studentCursor = db.query("users", new String[]{"_id"}, "name=?", new String[]{studentName}, null, null, null);
        int studentId = -1;
        if (studentCursor.moveToFirst()) {
            studentId = studentCursor.getInt(studentCursor.getColumnIndex("_id"));
        }
        studentCursor.close();

        if (studentId == -1) {
            Toast.makeText(this, "未找到该学生", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据课程名称查询课程ID
        Cursor courseCursor = db.query("courses", new String[]{"course_id"}, "course_name=?", new String[]{courseName}, null, null, null);
        int courseId = -1;
        if (courseCursor.moveToFirst()) {
            courseId = courseCursor.getInt(courseCursor.getColumnIndex("course_id"));
        }
        courseCursor.close();

        if (courseId == -1) {
            Toast.makeText(this, "未找到该课程", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新成绩
        SQLiteDatabase writableDb = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);

        int rowsAffected = writableDb.update(
                "grades",
                values,
                "student_id=? AND course_id=?",
                new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            searchScores();
            clearInputs();
        } else {
            Toast.makeText(this, "更新失败，可能是因为找不到记录", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteScore() {
        String studentName = editTextStuName.getText().toString();
        String courseName = editTextCourseName.getText().toString();

        if (studentName.isEmpty() || courseName.isEmpty()) {
            Toast.makeText(this, "请填写学生姓名和课程名称", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 根据姓名查询学生ID
        Cursor studentCursor = db.query(
                "users",
                new String[]{"_id"},
                "name=?",
                new String[]{studentName},
                null,
                null,
                null);
        int studentId = -1;
        if (studentCursor.moveToFirst()) {
            studentId = studentCursor.getInt(studentCursor.getColumnIndex("_id"));
        }
        studentCursor.close();
        if (studentId == -1) {
            Toast.makeText(this, "未找到该学生", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据课程名称查询课程ID
        Cursor courseCursor = db.query("courses", new String[]{"course_id"}, "course_name=?", new String[]{courseName}, null, null, null);
        int courseId = -1;
        if (courseCursor.moveToFirst()) {
            courseId = courseCursor.getInt(courseCursor.getColumnIndex("course_id"));
        }
        courseCursor.close();

        if (courseId == -1) {
            Toast.makeText(this, "未找到该课程", Toast.LENGTH_SHORT).show();
            return;
        }

        // 删除成绩
        SQLiteDatabase writableDb = myHelper.getWritableDatabase();
        int rowsAffected = writableDb.delete("grades", "student_id=? AND course_id=?", new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "删除失败，可能是因为找不到记录", Toast.LENGTH_SHORT).show();
        }
    }
    private void searchScores() {
        String studentName = editTextStuName.getText().toString();
        String courseName = editTextCourseName.getText().toString();

        SQLiteDatabase db = myHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!studentName.isEmpty()) {
            selection.append("student_id IN (SELECT _id FROM users WHERE name LIKE ?)");
            selectionArgs.add("%" + studentName + "%");
        }
        if (!courseName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_id IN (SELECT course_id FROM courses WHERE course_name LIKE ?)");
            selectionArgs.add("%" + courseName + "%");
        }

        Cursor cursor = db.query(
                "grades", // 表名
                new String[]{"grade_id AS _id", "student_id", "course_id", "score"},
                selection.toString(), // WHERE 子句
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
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
                        "users",
                        new String[]{"name"},
                        "_id=?", // WHERE 子句
                        new String[]{studentIdFromCursor},
                        null,
                        null,
                        null
                );
                Cursor cursor2 = db.query(
                        "courses",
                        new String[]{"course_name"},
                        "course_id=?", // WHERE 子句
                        new String[]{courseIdFromCursor},
                        null,
                        null,
                        null
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
    }
    // 清除输入框内容
    private void clearInputs() {
        editTextStuName.setText("");
        editTextCourseName.setText("");
        editTextScore.setText("");
    }
}