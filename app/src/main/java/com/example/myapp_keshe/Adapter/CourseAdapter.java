package com.example.myapp_keshe.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.myapp_keshe.R;

// 课程适配器类，继承自CursorAdapter
public class CourseAdapter extends CursorAdapter {
    // 构造函数
    public CourseAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // 创建新视图
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 使用布局填充器从布局资源中创建新的视图
        return LayoutInflater.from(context).inflate(R.layout.courselistview, parent, false);
    }

    // 绑定视图
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 获取所有文本视图
        TextView idTextView = view.findViewById(R.id.textViewCourseId);
        TextView nameTextView = view.findViewById(R.id.textViewCourseName);
        TextView codeTextView = view.findViewById(R.id.textViewCourseCode);
        TextView instructorTextView = view.findViewById(R.id.textViewInstructor);
        TextView creditsTextView = view.findViewById(R.id.textViewCredits);

        // 从数据库游标中获取数据
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("course_name"));
        String code = cursor.getString(cursor.getColumnIndex("course_code"));
        String instructor = cursor.getString(cursor.getColumnIndex("instructor"));
        int credits = cursor.getInt(cursor.getColumnIndex("credits"));

        // 将数据添加标签
        String labeledId = "课程ID: " + id;
        String labeledName = "课程名: " + name;
        String labeledCode = "课程代码: " + code;
        String labeledInstructor = "授课老师: " + instructor;
        String labeledCredits = "学分: " + credits;

        // 在对应的文本视图中设置数据
        idTextView.setText(labeledId);
        nameTextView.setText(labeledName);
        codeTextView.setText(labeledCode);
        instructorTextView.setText(labeledInstructor);
        creditsTextView.setText(labeledCredits);
    }
}