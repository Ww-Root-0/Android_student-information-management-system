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

import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    // 定义UI组件
    private EditText editTextId, editTextName;
    private Spinner spinnerClass, spinnerCourseName;
    private Button buttonSearch,buttonBack;
    private ListView listViewResults;
    private RadioGroup radioGroupQueryType;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.query);

        // 初始化数据库助手
        myHelper = new MyHelper(this);

        // 初始化UI组件
        init();

        // 填充下拉框内容
        populateSpinners();
    }

    // 初始化UI组件和设置事件监听器
    private void init() {
        editTextId = findViewById(R.id.editTextId); // 获取学号输入框
        editTextName = findViewById(R.id.editTextName); // 获取姓名输入框
        spinnerClass = findViewById(R.id.spinnerClass); // 获取班级下拉框
        spinnerCourseName = findViewById(R.id.spinnerCourseName); // 获取课程名称下拉框
        radioGroupQueryType = findViewById(R.id.radioGroupQueryType); // 获取单选按钮组
        buttonSearch = findViewById(R.id.buttonSearch); // 获取查询按钮
        buttonBack = findViewById(R.id.buttonBack); // 获取返回按钮
        listViewResults = findViewById(R.id.listViewResults); // 获取显示结果的列表视图

        // 设置查询按钮的点击事件监听器
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行查询操作
                search();
            }
        });
        // 设置返回按钮的点击事件监听器
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束当前活动，返回上一个活动
                finish();
            }
        });

        // 设置ListView的点击事件监听器
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项信息
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));

                // 启动UserDetailActivity并传递信息
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
    }

    // 填充下拉框内容
    private void populateSpinners() {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 填充班级下拉框
        List<String> classList = new ArrayList<>();
        Cursor cursor = db.query("users", new String[]{"class"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            classList.add(cursor.getString(cursor.getColumnIndexOrThrow("class")));
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        // 填充课程名称下拉框
        List<String> courseNameList = new ArrayList<>();
        cursor = db.query("courses", new String[]{"course_name"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            courseNameList.add(cursor.getString(cursor.getColumnIndexOrThrow("course_name")));
        }
        ArrayAdapter<String> courseNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNameList);
        courseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseName.setAdapter(courseNameAdapter);
    }

    // 查询方法
    private void search() {
        // 获取用户输入的查询条件
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String className = spinnerClass.getSelectedItem() != null ? spinnerClass.getSelectedItem().toString() : "";
        String courseName = spinnerCourseName.getSelectedItem() != null ? spinnerCourseName.getSelectedItem().toString() : "";

        // 获取查询类型
        int selectedId = radioGroupQueryType.getCheckedRadioButtonId();
        if (selectedId == -1) {
            // 显示提示信息，告知用户需要选择查询类型
            Toast.makeText(this, "请选择查询类型", Toast.LENGTH_SHORT).show();
            return; // 结束方法，不执行后续操作
        }

        // 获取可读的数据库实例
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 构建查询条件
        StringBuilder selection = new StringBuilder();
        if (!id.isEmpty()) {
            selection.append("_id = ").append(id);
        }
        if (!name.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("name LIKE '%").append(name).append("%'");
        }
        if (!className.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("class LIKE '%").append(className).append("%'");
        }
        if (!courseName.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("course_name LIKE '%").append(courseName).append("%'");
        }

        // 执行查询
        Cursor cursor;
        if (selectedId == R.id.radioStudent) {
            cursor = db.query(
                    "users",              // 表名
                    new String[]{"_id", "name", "gender", "phone", "class", "department", "age", "password"},  // 返回的列
                    selection.toString(), // WHERE 子句
                    null,                // WHERE 子句中的占位符的值
                    null,                // GROUP BY 子句
                    null,                // HAVING 子句
                    null                 // ORDER BY 子句
            );
        } else if (selectedId == R.id.radioCourse) {
            cursor = db.query(
                    "courses",              // 表名
                    new String[]{"course_id", "course_name", "course_code", "instructor", "credits"},  // 返回的列
                    selection.toString(), // WHERE 子句
                    null,                // WHERE 子句中的占位符的值
                    null,                // GROUP BY 子句
                    null,                // HAVING 子句
                    null                 // ORDER BY 子句
            );
        } else {
            cursor = db.query(
                    "grades",              // 表名
                    new String[]{"grade_id", "student_id", "course_id", "grade"},  // 返回的列
                    selection.toString(), // WHERE 子句
                    null,                // WHERE 子句中的占位符的值
                    null,                // GROUP BY 子句
                    null,                // HAVING 子句
                    null                 // ORDER BY 子句
            );
        }

        // 检查查询结果是否为空
        if (cursor.getCount() == 0) {
            // 显示提示信息，告知用户没有找到符合条件的信息
            Toast.makeText(this, "没有找到符合条件的信息", Toast.LENGTH_SHORT).show();
        } else {
            // 创建一个SimpleCursorAdapter来显示查询结果
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.listview, // 使用指定的简单列表项布局
                    cursor,
                    new String[]{"name", "gender", "age", "phone"}, // 显示学生姓名、性别、年龄、电话
                    new int[]{R.id.textViewName, R.id.textViewGender, R.id.textViewAge, R.id.textViewPhone}, // 映射到布局中的TextView
                    0
            );
            // 设置适配器到ListView
            listViewResults.setAdapter(adapter);
        }
    }
}