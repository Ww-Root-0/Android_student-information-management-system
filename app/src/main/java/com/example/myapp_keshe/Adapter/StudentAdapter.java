package com.example.myapp_keshe.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.myapp_keshe.R;

// 联系人适配器类，继承自CursorAdapter
public class StudentAdapter extends CursorAdapter {
    // 构造函数
    public StudentAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // 创建新视图
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 使用布局填充器从布局资源中创建新的视图
        return LayoutInflater.from(context).inflate(R.layout.stulistview, parent, false);
    }

    // 绑定视图
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 获取所有文本视图
        TextView idTextView = view.findViewById(R.id.textViewId);
        TextView nameTextView = view.findViewById(R.id.textViewName);
        TextView genderTextView = view.findViewById(R.id.textViewGender);
        TextView ageTextView = view.findViewById(R.id.textViewAge);
        TextView classTextView = view.findViewById(R.id.textViewClass);

        // 从数据库游标中获取数据
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String gender = cursor.getString(cursor.getColumnIndex("gender"));
        int age = cursor.getInt(cursor.getColumnIndex("age"));
        String className = cursor.getString(cursor.getColumnIndex("class"));

        // 将数据添加标签
        String labeledId = "ID: " + id;
        String labeledName = "Name: " + name;
        String labeledGender = "Gender: " + gender;
        String labeledAge = "Age: " + age;
        String labeledClass = "Class: " + className;

        // 在对应的文本视图中设置数据
        idTextView.setText(labeledId);
        nameTextView.setText(labeledName);
        genderTextView.setText(labeledGender);
        ageTextView.setText(labeledAge);
        classTextView.setText(labeledClass);
    }
}