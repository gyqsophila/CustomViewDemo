package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 拉动刷新view
 * Created by Alpha on 2016/7/23.
 */
public class RefeashListView extends ListView {
    /**
     * 下拉刷新状态
     */
    public static final int PULL_TO_REFEASH = 1;
    /**
     * 释放开始刷新状态
     */
    public static final int RELEASE_TO_REFEASH = 2;
    /**
     * 正在刷新状态
     */
    public static final int REFEASHING = 3;
    /**
     * 默认为下拉刷新状态
     */
    private int currentState=PULL_TO_REFEASH;

    private View mHeadView;
    private ImageView mArrowView;
    private ProgressBar mPB;
    private TextView mTitleView;
    private TextView mLastRefeashTimeView;
    /**
     * 头布局的显示高度
     */
    private int mHeadViewMeasuredHeight;
    /**
     * 箭头指向转上动画
     */
    private RotateAnimation rotateUpAnim;
    /**
     * 箭头转下动画
     */
    private RotateAnimation rotateDownAnim;
    /**
     * 按下位置的纵坐标
     */
    private float downY;
    /**
     * 刷新状态变化监听接口实例化
     */
    private onRefreshListener mListener;


    public RefeashListView(Context context) {
        super(context);
        initial();
    }

    public RefeashListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public RefeashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }

    /**
     * 初始化过程
     */
    private void initial() {
        initHeaderView();
        initAnimation();
        //initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeadView = View.inflate(getContext(), R.layout.layout_header_view, null);
        mArrowView = (ImageView) mHeadView.findViewById(R.id.iv_arrow);
        mPB = (ProgressBar) mHeadView.findViewById(R.id.header_pb);
        mTitleView = (TextView) mHeadView.findViewById(R.id.tv_title);
        mLastRefeashTimeView = (TextView) mHeadView.findViewById(R.id.tv_last_refeash_time);

        //手动设置测量的数据，也是实际的数据
        mHeadView.measure(0,0);
        //实际显示出来的高度
        mHeadViewMeasuredHeight = mHeadView.getMeasuredHeight();
        //默认不显示
        mHeadView.setPadding(0,-mHeadViewMeasuredHeight,0,0);
        //添加到头部布局
        addHeaderView(mHeadView);
    }

    /**
     * 初始化头布局的动画
     */
    private void initAnimation() {
        rotateUpAnim = new RotateAnimation(0f, -180f,//其实角度
                Animation.RELATIVE_TO_SELF, 0.5f,//横坐标
                Animation.RELATIVE_TO_SELF, 0.5f);//纵坐标
        rotateUpAnim.setDuration(300);
        rotateUpAnim.setFillAfter(true);//停留在动画结束后

        rotateDownAnim = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(300);
        rotateDownAnim.setFillAfter(true);
    }

    /**
     * 监听触摸动作更新UI和状态
     * @param ev 触摸动作
     * @return 是否消费触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动到新位置的纵坐标
                float moveY = ev.getY();
                if (currentState ==REFEASHING){
                    return super.onTouchEvent(ev);
                }

                float offset= moveY -downY;//移动偏移量
                //若果偏移量为正并且顶部item为第0个，才放大头部
                if (offset>0&&getFirstVisiblePosition()==0){
                    int paddingTop= (int) (-mHeadViewMeasuredHeight+offset);
                    //随着移动更新头布局显示的位置
                    mHeadView.setPadding(0,paddingTop,0,0);
                    //根据当前模式更新UI
                    if (paddingTop>0&&currentState!=RELEASE_TO_REFEASH){
                        Log.d("refeashListView","切换至释放刷新模式:"+paddingTop);
                        currentState=RELEASE_TO_REFEASH;
                        updateHeader();
                    }
                    else if (paddingTop<0&&currentState!=PULL_TO_REFEASH){
                        Log.d("refeashListView","切换至下拉刷新模式:"+paddingTop);
                        currentState=PULL_TO_REFEASH;
                        updateHeader();
                    }
                    return true;//放大头部，消费划动事件
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState==PULL_TO_REFEASH){
                    mHeadView.setPadding(0,-mHeadViewMeasuredHeight,0,0);
                }else if (currentState==RELEASE_TO_REFEASH){
                    mHeadView.setPadding(0, 0, 0, 0);
                    currentState=REFEASHING;
                    updateHeader();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更新头布局UI
     */
    private void updateHeader() {
        switch (currentState){
            case PULL_TO_REFEASH:
                mArrowView.startAnimation(rotateDownAnim);
                mTitleView.setText("下拉刷新");
                break;
            case RELEASE_TO_REFEASH:
                mArrowView.startAnimation(rotateUpAnim);
                mTitleView.setText("释放开始刷新");
                break;
            case REFEASHING:
                mArrowView.clearAnimation();
                mArrowView.setVisibility(INVISIBLE);
                mPB.setVisibility(VISIBLE);
                mTitleView.setText("正在刷新中");
                if (mListener!=null){
                    mListener.onRefeash();
                }
                break;
        }
    }

    /**
     * 刷新完成后的逻辑
     */
    public void onRefeashComplete(){
        currentState=PULL_TO_REFEASH;
        mTitleView.setText("下拉刷新");
        mHeadView.setPadding(0, -mHeadViewMeasuredHeight, 0, 0);
        mPB.setVisibility(INVISIBLE);
        mArrowView.setVisibility(VISIBLE);
        mLastRefeashTimeView.setText(getTime());
    }

    /**
     * 获取刷新时间
     * @return 时间
     */
    private String getTime() {
        long currentTime=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return "最后刷新时间："+sdf.format(currentTime);
    }

    /**
     * 刷新状态变化的监听器
     */
    public interface onRefreshListener{
        void onRefeash();
        //void onLoadMore();
    }

    /**
     * 设置刷新状态监听器
     * @param listener 监听接口实例
     */
    public void setOnRefeashListener(onRefreshListener listener){
        this.mListener=listener;
    }
}
