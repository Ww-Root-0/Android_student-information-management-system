package com.example.myapp_keshe.Pojo;

public class Score {
    private String score;
    private String studentName;
    private String courseName;

    public Score(String score, String studentName, String courseName) {
        this.score = score;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public String getScore() {
        return score;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }
}