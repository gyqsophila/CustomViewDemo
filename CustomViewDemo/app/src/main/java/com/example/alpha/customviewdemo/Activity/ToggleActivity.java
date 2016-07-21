package com.example.alpha.customviewdemo.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.UI.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开关视图
 * Created by Alpha on 2016/7/21.
 */
public class ToggleActivity extends AppCompatActivity {
    @BindView(R.id.toggle_button)
    ToggleButton toggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toggle_layout);
        ButterKnife.bind(this);
        toggleButton.setOnSwitchStateUpdateListener(new ToggleButton.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(getApplicationContext(), "state:"+state,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
