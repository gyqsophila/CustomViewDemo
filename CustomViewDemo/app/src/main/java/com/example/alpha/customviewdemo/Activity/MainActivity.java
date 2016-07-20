package com.example.alpha.customviewdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.alpha.customviewdemo.Activity.WheeledMenuActivity;
import com.example.alpha.customviewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.wheelDemo)
    Button wheelDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.wheelDemo)
    public void onClick() {
        Intent intent=new Intent(this,WheeledMenuActivity.class);
        startActivity(intent);
    }
}
