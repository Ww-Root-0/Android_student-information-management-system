package com.example.myapp_keshe.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.myapp_keshe.R;

// 联系人适配器类，继承自CursorAdapter
public class ContactAdapter extends CursorAdapter {
    // 构造函数
    public ContactAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // 创建新视图
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 使用布局填充器从布局资源中创建新的视图
        return LayoutInflater.from(context).inflate(R.layout.listview, parent, false);
    }

    // 绑定视图
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 获取姓名文本视图和电话文本视图
        TextView nameTextView = view.findViewById(R.id.textViewName);
        TextView phoneTextView = view.findViewById(R.id.textViewPhone);

        // 从数据库游标中获取姓名和电话
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String phone = cursor.getString(cursor.getColumnIndex("phone"));

        // 将姓名和电话添加标签
        String labeledName = "姓名: " + name;
        String labeledPhone = "电话: " + phone;

        // 在对应的文本视图中设置姓名和电话
        nameTextView.setText(labeledName);
        phoneTextView.setText(labeledPhone);
    }
}