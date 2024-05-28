package com.example.myapp_keshe.Control;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_keshe.R;

public class home extends AppCompatActivity {
    private Handler handler;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
}
