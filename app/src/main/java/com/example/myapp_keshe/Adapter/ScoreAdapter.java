package com.example.myapp_keshe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapp_keshe.Pojo.Score;
import com.example.myapp_keshe.R;

import java.util.List;

// 课程适配器类，继承自ArrayAdapter
public class ScoreAdapter extends ArrayAdapter<Score> {

    private Context context;
    private List<Score> scores;

    // 构造函数
    public ScoreAdapter(Context context, List<Score> scores) {
        super(context, 0, scores);
        this.context = context;
        this.scores = scores;
    }

    // 创建新视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 如果视图为空，则进行填充
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.scorelistview, parent, false);
        }

        // 获取当前的Score对象
        Score currentScore = scores.get(position);

        // 获取所有文本视图
        TextView textViewStudentName = convertView.findViewById(R.id.textViewStudentName);
        TextView textViewCourseName = convertView.findViewById(R.id.textViewCourseName);
        TextView textViewScore = convertView.findViewById(R.id.textViewScore);

        // 将数据添加标签
        String labeledstuName = "姓名: " + currentScore.getStudentName();
        String labeledcourseName = "课程名: " + currentScore.getCourseName();
        String labeledscore = "分数: " + currentScore.getScore();

        // 在对应的文本视图中设置数据
        textViewStudentName.setText(labeledstuName);
        textViewCourseName.setText(labeledcourseName);
        textViewScore.setText(labeledscore);

        return convertView;
    }
}