// 定义注册Activity的包名
package com.example.myapp_keshe.Control;

// 导入所需的类
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapp_keshe.Date.MyHelper;
import com.example.myapp_keshe.R;

// RegisterActivity类继承自AppCompatActivity类
public class RegisterActivity extends AppCompatActivity {

    // 声明各种UI组件的变量
    private EditText editTextName, editTextPhone, editTextClass, editTextPassword, editTextAge, editTextDepartment;
    private RadioGroup radioGroupGender;
    private Button buttonRegister, buttonBack;
    // 声明MyHelper类型的变量myHelper，用于数据库操作
    private MyHelper myHelper;

    // onCreate方法是Activity生命周期中的一部分，用于初始化Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        myHelper = new MyHelper(this);
        init();
    }

    private void init() {
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextClass = findViewById(R.id.editTextClass);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAge = findViewById(R.id.editTextAge);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBack = findViewById(R.id.buttonBack);

        // 注册事件监听器
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        // 为返回按钮设置点击事件监听器
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // registerUser方法用于处理注册逻辑
    private void registerUser() {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String className = editTextClass.getText().toString();
        String password = editTextPassword.getText().toString();
        String age = editTextAge.getText().toString();
        String department = editTextDepartment.getText().toString();
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        // 根据ID找到选中的性别单选按钮，并获取性别信息
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton.getText().toString();

        // 检查所有是否已填写
        if (name.isEmpty() || phone.isEmpty() || className.isEmpty() || password.isEmpty()
                || gender.isEmpty() || age.isEmpty() || department.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = myHelper.getWritableDatabase();
        // 使用ContentValues存储要插入的数据
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("phone", phone);
        values.put("class", className);
        values.put("password", password);
        values.put("age", age);
        values.put("department", department);

        // 插入数据，并获取ID
        long newRowId = db.insert("users", null, values);
        // 注册是否成功
        if (newRowId == -1) {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            // 注册成功后跳转到登录Activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        db.close();
    }
}