package com.example.myapp_keshe.Control;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

public class ClassQueryActivity extends AppCompatActivity {

    // 定义UI组件
    private EditText editTextClass;
    private Button buttonSearch;
    private ListView listViewStudents;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.stuclass);

        // 初始化数据库助手
        myHelper = new MyHelper(this);

        // 初始化UI组件
        init();
    }

    // 初始化UI组件和设置事件监听器
    private void init() {
        editTextClass = findViewById(R.id.editTextClass); // 获取班级输入框
        buttonSearch = findViewById(R.id.buttonSearch); // 获取查询按钮
        listViewStudents = findViewById(R.id.listViewStudents); // 获取显示学生信息的列表视图

        // 设置查询按钮的点击事件监听器
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行查询学生信息的操作
                searchStudents();
            }
        });
    }

    // 查询学生信息的方法
    private void searchStudents() {
        // 获取用户输入的班级名称，并去除前后空格
        String className = editTextClass.getText().toString().trim();

        // 检查班级名称是否为空
        if (className.isEmpty()) {
            // 显示提示信息，告知用户需要输入班级名称
            Toast.makeText(this, "请输入班级名称", Toast.LENGTH_SHORT).show();
            return; // 结束方法，不执行后续操作
        }

        // 获取可读的数据库实例
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 查询数据库中的学生信息
        Cursor cursor = db.query(
                "users",              // 表名
                new String[]{"_id", "name", "gender", "phone", "class", "department"},  // 返回的列
                "class=?",  // WHERE 子句
                new String[]{className},  // WHERE 子句中的占位符的值
                null,                // GROUP BY 子句
                null,                // HAVING 子句
                null                 // ORDER BY 子句
        );

        // 检查查询结果是否为空
        if (cursor.getCount() == 0) {
            // 显示提示信息，告知用户没有找到该班级的学生信息
            Toast.makeText(this, "没有找到该班级的学生信息", Toast.LENGTH_SHORT).show();
        } else {
            // 创建一个SimpleCursorAdapter来显示查询结果
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.listview, // 使用指定的简单列表项布局
                    cursor,
                    new String[]{"name", "phone"}, // 显示学生姓名和电话
                    new int[]{R.id.textViewName, R.id.textViewPhone}, // 映射到布局中的TextView
                    0
            );
            // 设置适配器到ListView
            listViewStudents.setAdapter(adapter);
        }
    }
}