package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageButton;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.UI.SlideMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 侧滑菜单界面
 * Created by Alpha on 2016/7/25.
 */
public class SlideMenuAvtiviy extends AppCompatActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.slideMenu)
    SlideMenu slideMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_slide_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        slideMenu.switchState();
    }
}
