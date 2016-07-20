package com.example.alpha.customviewdemo.Utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * 动画工具
 * Created by Alpha on 2016/7/20.
 */
public class AnimationUtils {
    //正在播放的动画
    public static int runningAnmationCount=0;
    //旋转出去的动画
    public static void RotateOutAnimation(RelativeLayout layout,long delaytime){
        int childCount=layout.getChildCount();
        //若隐藏动画则禁用所有子view
        for (int i=0;i<childCount;i++){
            layout.getChildAt(i).setEnabled(false);
        }

        RotateAnimation animation=new RotateAnimation(
                0f,-180f,//起始角度，逆时针
                Animation.RELATIVE_TO_SELF,0.5f,//旋转中心横坐标
                Animation.RELATIVE_TO_SELF,1.0f);//旋转中心纵坐标
        animation.setDuration(500);//播放周期
        animation.setFillAfter(true);//停止在动画结束的地方
        animation.setStartOffset(delaytime);//播放延时
        animation.setAnimationListener(new MyAnimationListener());
        layout.startAnimation(animation);
    }

    //旋转进来的动画
    public static void RotateInAnimation(RelativeLayout layout,long delaytime){
        int childCount=layout.getChildCount();
        //若隐藏动画则禁用所有子view
        for (int i=0;i<childCount;i++){
            layout.getChildAt(i).setEnabled(true);
        }

        RotateAnimation animation=new RotateAnimation(
                -180f,0f,//起始角度，逆时针
                Animation.RELATIVE_TO_SELF,0.5f,//旋转中心横坐标
                Animation.RELATIVE_TO_SELF,1.0f);//旋转中心纵坐标
        animation.setDuration(500);//播放周期
        animation.setFillAfter(true);//停止在动画结束的地方
        animation.setStartOffset(delaytime);//播放延时
        animation.setAnimationListener(new MyAnimationListener());
        layout.startAnimation(animation);
    }

    private static class MyAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            runningAnmationCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            runningAnmationCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
