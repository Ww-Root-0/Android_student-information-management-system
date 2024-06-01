package com.example.myapp_keshe.Control;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.Adapter.StudentAdapter;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    // 定义UI组件
    private EditText editTextName, editTextAge, editTextPhone, editTextDepartment;
    private Spinner spinnerGender, spinnerClass;
    private Button buttonAdd, buttonUpdate, buttonDelete, buttonSearch, buttonBack;
    private ListView listViewResults;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.student);

        // 初始化数据库助手
        myHelper = new MyHelper(this);

        // 初始化UI组件
        init();

        // 填充下拉框内容
        populateSpinners();
    }

    // 初始化UI组件和设置事件监听器
    private void init() {
        editTextName = findViewById(R.id.editTextName);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextAge = findViewById(R.id.editTextAge);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        spinnerClass = findViewById(R.id.spinnerClass);
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
                addStudent();
            }
        });

        // 设置更新按钮的点击事件监听器
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        // 设置删除按钮的点击事件监听器
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });

        // 设置查询按钮的点击事件监听器
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudents();
            }
        });

        // 设置ListView的点击事件监听器
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));

                editTextName.setText(name);
                setSpinnerSelection(spinnerGender, gender);
                editTextAge.setText(String.valueOf(age));
                setSpinnerSelection(spinnerClass, className);
                editTextPhone.setText(phone);
                editTextDepartment.setText(department);
            }
        });
    }

    // 填充下拉框内容
    private void populateSpinners() {
        SQLiteDatabase db = myHelper.getReadableDatabase();

        // 填充班级下拉框
        List<String> classList = new ArrayList<>();
        Cursor cursor = db.query("users", new String[]{"class"}, null, null, "class", null, null);
        while (cursor.moveToNext()) {
            classList.add(cursor.getString(cursor.getColumnIndexOrThrow("class")));
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        // 填充性别下拉框
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
    }

    // 添加学生信息
    private void addStudent() {
        String name = editTextName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String ageStr = editTextAge.getText().toString().trim();
        String className = spinnerClass.getSelectedItem().toString();
        String phone = editTextPhone.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        Integer age = null;
        if (!ageStr.isEmpty()) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "年龄必须是一个有效的数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("INSERT INTO users (name, gender, age, class, phone, department) VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{name.isEmpty() ? null : name, gender.isEmpty() ? null : gender, age, className.isEmpty() ? null : className, phone.isEmpty() ? null : phone, department.isEmpty() ? null : department});
        Toast.makeText(this, "学生信息添加成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 更新学生信息
    private void updateStudent() {
        String name = editTextName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String ageStr = editTextAge.getText().toString().trim();
        String className = spinnerClass.getSelectedItem().toString();
        String phone = editTextPhone.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        Integer age = null;
        if (!ageStr.isEmpty()) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "年龄必须是一个有效的数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("UPDATE users SET gender = ?, age = ?, class = ?, phone = ?, department = ? WHERE name = ?",
                new Object[]{gender.isEmpty() ? null : gender, age, className.isEmpty() ? null : className, phone.isEmpty() ? null : phone, department.isEmpty() ? null : department, name.isEmpty() ? null : name});
        Toast.makeText(this, "学生信息更新成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 删除学生信息
    private void deleteStudent() {
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.execSQL("DELETE FROM users WHERE name = ?", new Object[]{name});
        Toast.makeText(this, "学生信息删除成功", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    // 查询学生信息
    private void searchStudents() {
        String name = editTextName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem() != null ? spinnerGender.getSelectedItem().toString() : "";
        String ageStr = editTextAge.getText().toString().trim();
        String className = spinnerClass.getSelectedItem() != null ? spinnerClass.getSelectedItem().toString() : "";
        String phone = editTextPhone.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (!name.isEmpty()) {
            selection.append("name LIKE ?");
            selectionArgs.add("%" + name + "%");
        }
        if (!gender.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("gender = ?");
            selectionArgs.add(gender);
        }
        if (!ageStr.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("age = ?");
            selectionArgs.add(ageStr);
        }
        if (!className.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("class = ?");
            selectionArgs.add(className);
        }
        if (!phone.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("phone LIKE ?");
            selectionArgs.add("%" + phone + "%");
        }
        if (!department.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append("department LIKE ?");
            selectionArgs.add("%" + department + "%");
        }

        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users", // 表名
                new String[]{"_id", "name", "gender", "age", "class", "phone", "department"}, // 返回的列
                selection.toString(), // WHERE 子句
                selectionArgs.toArray(new String[0]), // WHERE 子句中的占位符的值
                null, // GROUP BY 子句
                null, // HAVING 子句
                null // ORDER BY 子句
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "没有找到符合条件的信息", Toast.LENGTH_SHORT).show();
        } else {
            // 使用自定义的StudentAdapter来显示数据
            StudentAdapter adapter = new StudentAdapter(this, cursor);
            listViewResults.setAdapter(adapter);
        }
    }

    // 清除输入框内容
    private void clearInputs() {
        editTextName.setText("");
        spinnerGender.setSelection(0);
        editTextAge.setText("");
        spinnerClass.setSelection(0);
        editTextPhone.setText("");
        editTextDepartment.setText("");
    }

    // 设置Spinner的选中项
    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}