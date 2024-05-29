package com.example.myapp_keshe.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MyHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        db.execSQL("CREATE TABLE users (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class TEXT, " +
                "name TEXT, " +
                "password TEXT, " +
                "gender TEXT, " +
                "age INTEGER, " +
                "date_of_birth TEXT, " +
                "phone TEXT, " +
                "email TEXT" +
                ")");

        // 创建课程表
        db.execSQL("CREATE TABLE courses (" +
                "course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "course_name TEXT, " +
                "course_code TEXT, " +
                "instructor TEXT, " +
                "credits INTEGER" +
                ")");

        // 创建成绩表，并关联用户表和课程表
        db.execSQL("CREATE TABLE grades (" +
                "grade_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "course_id INTEGER, " +
                "grade TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES users(_id), " +
                "FOREIGN KEY(course_id) REFERENCES courses(course_id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库的逻辑
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS courses");
        db.execSQL("DROP TABLE IF EXISTS grades");
        onCreate(db);
    }
}