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

    private EditText editTextClass;
    private Button buttonSearch;
    private ListView listViewStudents;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stuclass);

        myHelper = new MyHelper(this);
        init();
    }

    private void init() {
        editTextClass = findViewById(R.id.editTextClass);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewStudents = findViewById(R.id.listViewStudents);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudents();
            }
        });
    }

    private void searchStudents() {
        String className = editTextClass.getText().toString().trim();

        if (className.isEmpty()) {
            Toast.makeText(this, "请输入班级名称", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users",              // 表名
                new String[]{"_id", "name", "gender", "phone", "class", "department"},  // 返回的列
                "class=?",  // WHERE 子句
                new String[]{className},  // WHERE 子句中的占位符的值
                null,                // GROUP BY
                null,                // HAVING
                null                 // ORDER BY
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到该班级的学生信息", Toast.LENGTH_SHORT).show();
        } else {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"name", "phone"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            listViewStudents.setAdapter(adapter);
        }
    }
}