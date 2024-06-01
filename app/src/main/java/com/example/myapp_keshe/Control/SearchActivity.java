package com.example.myapp_keshe.Control;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.Adapter.CourseAdapter;
import com.example.myapp_keshe.Adapter.ScoreAdapter;
import com.example.myapp_keshe.Adapter.StudentAdapter;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.Pojo.Score;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextId, editTextName;
    private Spinner spinnerClass, spinnerCourseName;
    private Button buttonSearch, buttonBack;
    private ListView listViewResults;
    private RadioGroup radioGroupQueryType;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        myHelper = new MyHelper(this);

        init();
        populateSpinners();
    }

    private void init() {
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerCourseName = findViewById(R.id.spinnerCourseName);
        radioGroupQueryType = findViewById(R.id.radioGroupQueryType);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonBack = findViewById(R.id.buttonBack);
        listViewResults = findViewById(R.id.listViewResults);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));

                Intent intent = new Intent(SearchActivity.this, UserManagementActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("password", password);
                intent.putExtra("gender", gender);
                intent.putExtra("age", age);
                intent.putExtra("phone", phone);
                intent.putExtra("class", className);
                intent.putExtra("department", department);
                startActivity(intent);
            }
        });

        radioGroupQueryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioStudent) {
                    spinnerCourseName.setEnabled(false);
                    spinnerClass.setEnabled(true);
                    editTextId.setHint("学号");
                    editTextName.setHint("姓名");
                } else if (checkedId == R.id.radioCourse) {
                    spinnerCourseName.setEnabled(true);
                    spinnerClass.setEnabled(false);
                    editTextId.setHint("课程代码");
                    editTextName.setHint("授课老师");
                } else {
                    spinnerCourseName.setEnabled(true);
                    spinnerClass.setEnabled(true);
                    editTextId.setHint("学号");
                    editTextName.setHint("姓名");
                }
            }
        });
    }

    private void populateSpinners() {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        List<String> classList = new ArrayList<>();
        classList.add("");
        Cursor cursor = db.query("users", new String[]{"class"}, null, null, "class", null, null);
        while (cursor.moveToNext()) {
            classList.add(cursor.getString(cursor.getColumnIndexOrThrow("class")));
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        List<String> courseNameList = new ArrayList<>();
        courseNameList.add("");
        cursor = db.query("courses", new String[]{"course_name"}, null, null, "course_name", null, null);
        while (cursor.moveToNext()) {
            courseNameList.add(cursor.getString(cursor.getColumnIndexOrThrow("course_name")));
        }
        ArrayAdapter<String> courseNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNameList);
        courseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseName.setAdapter(courseNameAdapter);
    }

    private void search() {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String className = spinnerClass.getSelectedItem() != null ? spinnerClass.getSelectedItem().toString() : "";
        String courseName = spinnerCourseName.getSelectedItem() != null ? spinnerCourseName.getSelectedItem().toString() : "";

        int selectedId = radioGroupQueryType.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "请选择查询类型", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId == R.id.radioStudent) {
            searchStudents(id, name, className);
        } else if (selectedId == R.id.radioCourse) {
            searchCourses(id, name, courseName);
        } else if (selectedId == R.id.radioGrade) {
            searchScores(id, name, className, courseName);
        }
    }

    private void searchStudents(String id, String name, String className) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!id.isEmpty()) {
            selection.append("_id = ?");
            selectionArgs.add(id);
        }
        if (!name.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("name LIKE ?");
            selectionArgs.add("%" + name + "%");
        }
        if (!className.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("class LIKE ?");
            selectionArgs.add("%" + className + "%");
        }

        Cursor cursor = db.query(
                "users",
                new String[]{"_id", "name", "gender", "phone", "class", "department", "age", "password"},
                selection.toString(),
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的学生信息", Toast.LENGTH_SHORT).show();
        } else {
            StudentAdapter adapter = new StudentAdapter(this, cursor);
            listViewResults.setAdapter(adapter);
        }
    }

    private void searchCourses(String id, String name, String courseName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!id.isEmpty()) {
            selection.append("course_code = ?");
            selectionArgs.add(id);
        }
        if (!name.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("instructor LIKE ?");
            selectionArgs.add("%" + name + "%");
        }
        if (!courseName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_name LIKE ?");
            selectionArgs.add("%" + courseName + "%");
        }

        Cursor cursor = db.query(
                "courses",
                new String[]{"course_id AS _id", "course_name", "course_code", "instructor", "credits"},
                selection.toString(),
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的课程信息", Toast.LENGTH_SHORT).show();
        } else {
            CourseAdapter adapter = new CourseAdapter(this, cursor);
            listViewResults.setAdapter(adapter);
        }
    }

    private void searchScores(String studentId, String studentName, String className, String courseName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!studentId.isEmpty()) {
            selection.append("student_id LIKE ?");
            selectionArgs.add("%" + studentId + "%");
        }
        if (!studentName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("student_id IN (SELECT _id FROM users WHERE name LIKE ?)");
            selectionArgs.add("%" + studentName + "%");
        }
        if (!className.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("student_id IN (SELECT _id FROM users WHERE class LIKE ?)");
            selectionArgs.add("%" + className + "%");
        }
        if (!courseName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_id IN (SELECT course_id FROM courses WHERE course_name LIKE ?)");
            selectionArgs.add("%" + courseName + "%");
        }

        Cursor cursor = db.query(
                "grades",
                new String[]{"grade_id AS _id", "student_id", "course_id", "score"},
                selection.toString(),
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
                        "_id=?",
                        new String[]{studentIdFromCursor},
                        null,
                        null,
                        null
                );
                Cursor cursor2 = db.query(
                        "courses",
                        new String[]{"course_name"},
                        "course_id=?",
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
}