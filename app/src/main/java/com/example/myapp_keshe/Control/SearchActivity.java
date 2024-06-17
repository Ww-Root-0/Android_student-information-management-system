package com.example.myapp_keshe.Control;// 导入所需的类和包
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapp_keshe.Adapter.CourseAdapter;
import com.example.myapp_keshe.Adapter.ScoreAdapter;
import com.example.myapp_keshe.Adapter.StudentAdapter;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.Pojo.Score;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    // 定义Activity中使用的各种视图组件
    private EditText editTextId, editTextName;
    private Spinner spinnerClass, spinnerCourseName;
    private Button buttonSearch;
    private ListView listViewResults;
    private RadioGroup radioGroupQueryType;
    private MyHelper myHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search); // 设置Activity的布局

        myHelper = new MyHelper(this); // 初始化数据库帮助类

        init(); // 初始化视图组件
        populateSpinners(); // 填充下拉列表
    }

    // 初始化视图组件和设置事件监听器
    private void init() {
        // 通过findViewById获取布局文件中定义的视图组件
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerCourseName = findViewById(R.id.spinnerCourseName);
        radioGroupQueryType = findViewById(R.id.radioGroupQueryType);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewResults = findViewById(R.id.listViewResults);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); // 设置工具栏
        // 显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // 设置返回按钮的点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 设置搜索按钮的点击事件
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(); // 调用search方法执行搜索
            }
        });

        // 设置单选按钮组的选择事件监听器
        radioGroupQueryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据选择的查询类型，调整界面上的输入框和下拉列表的可用状态
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

    // 填充下拉列表
    private void populateSpinners() {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 查询班级列表，填充班级下拉列表
        List<String> classList = new ArrayList<>();
        classList.add("");
        Cursor cursor = db.query("users", new String[]{"class"}, null, null, "class", null, null);
        while (cursor.moveToNext()) {
            classList.add(cursor.getString(cursor.getColumnIndexOrThrow("class")));
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        // 查询课程名称列表，填充课程名称下拉列表
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

    // 根据用户输入和选择的查询类型执行搜索
    private void search() {
        // 获取用户输入的查询条件
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String className = spinnerClass.getSelectedItem() != null ? spinnerClass.getSelectedItem().toString() : "";
        String courseName = spinnerCourseName.getSelectedItem() != null ? spinnerCourseName.getSelectedItem().toString() : "";

        // 获取选择的查询类型
        int selectedId = radioGroupQueryType.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "请选择查询类型", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据查询类型调用相应的搜索方法
        if (selectedId == R.id.radioStudent) {
            searchStudents(id, name, className);
        } else if (selectedId == R.id.radioCourse) {
            searchCourses(id, name, courseName);
        } else if (selectedId == R.id.radioGrade) {
            searchScores(id, name, className, courseName);
        }
    }

    // 搜索学生信息的方法
    private void searchStudents(String id, String name, String className) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 构建查询条件
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        // 根据用户输入添加查询条件
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

        // 执行查询
        Cursor cursor = db.query(
                "users",
                new String[]{"_id", "name", "gender", "phone", "class", "department", "age", "password"},
                selection.toString(),
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        // 根据查询结果更新ListView
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的学生信息", Toast.LENGTH_SHORT).show();
        } else {
            StudentAdapter adapter = new StudentAdapter(this, cursor);
            listViewResults.setAdapter(adapter);

            // 设置ListView的点击事件，点击时跳转到学生详情页面
            listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    // 获取选中学生的详细信息
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                    String className = cursor.getString(cursor.getColumnIndexOrThrow("class"));
                    String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));

                    // 将学生信息传递给详情页面
                    Intent intent = new Intent(SearchActivity.this, StudentDetailActivity.class);
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
        }
    }

    // 搜索课程信息的方法
    private void searchCourses(String id, String name, String courseName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 构建查询条件
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        // 根据用户输入添加查询条件
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

        // 执行查询
        Cursor cursor = db.query(
                "courses",
                new String[]{"course_id AS _id", "course_name", "course_code", "instructor", "credits"},
                selection.toString(),
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        // 根据查询结果更新ListView
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的课程信息", Toast.LENGTH_SHORT).show();
        } else {
            CourseAdapter adapter = new CourseAdapter(this, cursor);
            listViewResults.setAdapter(adapter);

            // 设置ListView的点击事件，点击时跳转到课程详情页面
            listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    // 获取选中课程的详细信息
                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    String courseName = cursor.getString(cursor.getColumnIndexOrThrow("course_name"));
                    String courseCode = cursor.getString(cursor.getColumnIndexOrThrow("course_code"));
                    String instructor = cursor.getString(cursor.getColumnIndexOrThrow("instructor"));
                    int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));

                    // 将课程信息传递给详情页面
                    Intent intent = new Intent(SearchActivity.this, CourseDetailActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("courseCode", courseCode);
                    intent.putExtra("instructor", instructor);
                    intent.putExtra("credits", credits);
                    startActivity(intent);
                }
            });
        }
    }

    // 搜索成绩的方法
    private void searchScores(String studentId, String studentName, String className, String courseName) {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 构建查询条件
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        // 根据用户输入添加查询条件
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

        // 执行查询
        Cursor cursor = db.query(
                "grades",
                new String[]{"grade_id AS _id", "student_id", "course_id", "score"},
                selection.toString(),
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );

        // 根据查询结果更新ListView
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的成绩", Toast.LENGTH_SHORT).show();
        } else {
            List<Score> scoreList = new ArrayList<>();
            while (cursor.moveToNext()) {
                // 获取成绩及相关学生和课程信息
                String score = cursor.getString(cursor.getColumnIndex("score"));
                String studentIdFromCursor = cursor.getString(cursor.getColumnIndex("student_id"));
                String courseIdFromCursor = cursor.getString(cursor.getColumnIndex("course_id"));

                // 查询学生姓名和课程名称
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

            // 设置ListView的点击事件，点击时跳转到成绩详情页面
            listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Score score = (Score) parent.getItemAtPosition(position);

                    // 将成绩信息传递给详情页面
                    Intent intent = new Intent(SearchActivity.this, ScoreDetailActivity.class);
                    intent.putExtra("studentName", score.getStudentName());
                    intent.putExtra("courseName", score.getCourseName());
                    intent.putExtra("score", score.getScore());
                    startActivity(intent);
                }
            });
        }
    }
}