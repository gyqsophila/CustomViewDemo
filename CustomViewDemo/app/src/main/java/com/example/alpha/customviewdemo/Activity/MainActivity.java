package com.example.alpha.customviewdemo.Activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alpha.customviewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.wheelDemo)
    Button wheelDemo;
    @BindView(R.id.carousel)
    Button carousel;
    @BindView(R.id.popup_window)
    Button popup_window;
    @BindView(R.id.toggle_button)
    Button toggle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.wheelDemo, R.id.carousel,R.id.popup_window,R.id.toggle_button})
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.wheelDemo:
                intent.setClass(this,WheeledMenuActivity.class);
                break;
            case R.id.carousel:
                intent.setClass(this,CarouselActivity.class);
                break;
            case R.id.popup_window:
                intent.setClass(this,PopupWindowActivity.class);
                break;
            case R.id.toggle_button:
                intent.setClass(this,ToggleActivity.class);
                break;
        }
        startActivity(intent);
    }
}
