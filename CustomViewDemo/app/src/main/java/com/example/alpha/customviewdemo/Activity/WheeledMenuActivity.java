package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.Utils.AnimationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 轮式菜单
 * Created by Alpha on 2016/7/20.
 */
public class WheeledMenuActivity extends AppCompatActivity {

    private boolean Level1Displayed=true;
    private boolean Level2Displayed=true;
    private boolean Level3Displayed=true;

    @BindView(R.id.home_level1)
    ImageButton homeLevel1;
    @BindView(R.id.rl_levle1)
    RelativeLayout rlLevle1;
    @BindView(R.id.menu_levle2)
    ImageButton menuLevle2;
    @BindView(R.id.rl_levle2)
    RelativeLayout rlLevle2;
    @BindView(R.id.rl_levle3)
    RelativeLayout rlLevle3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelmenu);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.home_level1, R.id.menu_levle2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_level1:
                //能点击则一级视图当然是显示的
                if (Level2Displayed){//再移除二级视图
                    long delay=0;
                    if (Level3Displayed){//先移除三级视图
                        AnimationUtils.RotateOutAnimation(rlLevle3,0);
                        Level3Displayed=false;
                        delay+=200;
                    }
                    AnimationUtils.RotateOutAnimation(rlLevle2,delay);
                }else {//二级视图为显示则显示二级视图
                    AnimationUtils.RotateInAnimation(rlLevle2,0);
                }
                Level2Displayed=!Level2Displayed;
                break;
            case R.id.menu_levle2:
                if (Level3Displayed){//若三级显示则移除三级菜单
                    AnimationUtils.RotateOutAnimation(rlLevle3,0);
                }else {//若为显示则移进来
                    AnimationUtils.RotateInAnimation(rlLevle3,0);
                }
                Level3Displayed=!Level3Displayed;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_MENU){
            if (AnimationUtils.runningAnmationCount>0){
                return true;
            }
            if (Level1Displayed){
                long delay=0;
                if (Level3Displayed){
                    AnimationUtils.RotateOutAnimation(rlLevle3,delay);
                    Level3Displayed=false;
                    delay+=200;
                }
                if (Level2Displayed){
                    AnimationUtils.RotateOutAnimation(rlLevle2,delay);
                    Level2Displayed=false;
                    delay+=200;
                }
                AnimationUtils.RotateOutAnimation(rlLevle1,delay);
            }else {
                AnimationUtils.RotateInAnimation(rlLevle1,0);
                AnimationUtils.RotateInAnimation(rlLevle2,200);
                AnimationUtils.RotateInAnimation(rlLevle3,400);
                Level2Displayed=true;
                Level3Displayed=true;
            }
            Level1Displayed=!Level1Displayed;
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
