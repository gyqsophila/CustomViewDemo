package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 侧滑菜单
 * Created by Alpha on 2016/7/25.
 */
public class SlideMenu extends ViewGroup {

    private static final String TAG = "SlideMenuTest";
    private float downX;
    private float downY;
    private float moveX;
    public static final int MENU_STATE = 0;
    public static final int MAIN_STATE = 1;
    private int currentState = 1;
    private Scroller scroller;
    private float downedX;
    private static final int SWITCH_DISTENCE = 150;
    private static final int INTERCEPT_DISTENCE=50;

    public SlideMenu(Context context) {
        super(context);
        initial();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }

    /**
     * 初始化滚动器
     */
    private void initial() {
        scroller = new Scroller(getContext());
    }

    /**
     * 测量并设置所有子view的宽高
     *
     * @param widthMeasureSpec  宽度
     * @param heightMeasureSpec 高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View leftMenu = getChildAt(0);
        //制定左面板宽高
        leftMenu.measure(leftMenu.getLayoutParams().width, leftMenu.getLayoutParams().height);
        View mainContent = getChildAt(1);
        //制定主面板宽高
        mainContent.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 摆放布局
     *
     * @param changed 当前控件的位置、尺寸是否变化
     * @param l       左边距
     * @param t       上边距
     * @param r       右边界
     * @param b       下边界
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View leftMenu = getChildAt(0);
        Log.d(TAG, String.valueOf(leftMenu.getMeasuredWidth()));
        leftMenu.layout(-leftMenu.getMeasuredWidth(), 0, 0, b);//默认隐藏
        getChildAt(1).layout(l, t, r, b);
    }

    /**
     * 根据触摸位置变化判断菜单开合状态
     *
     * @param event 触摸动作
     * @return 消费事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downedX = downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                //将要发生的偏移量
                int scrollX = (int) (downX - moveX);
                //计算将要滚动到位置，判断是否超出合理边界
                int newScrollPositionX = getScrollX() + scrollX;
                if (newScrollPositionX < -getChildAt(0).getMeasuredWidth()) {//限定左边界
                    scrollTo(-getChildAt(0).getMeasuredWidth(), 0);//直接滚动到指定位置
                } else if (newScrollPositionX > 0) {//限定右边界
                    scrollTo(0, 0);
                } else {//使变化生效
                    scrollBy(scrollX, 0);//在此前基础上移动
                }
                downX = moveX;//恢复开始的X值
                break;
            case MotionEvent.ACTION_UP:
                int leftCenter = (int) (-getChildAt(0).getMeasuredWidth() / 2.0f);
                if (getScrollX() < leftCenter) {
                    currentState = MENU_STATE;
                } else {
                    currentState = MAIN_STATE;
                }

                swipeToSwitch(event.getX());

                updateCurrentContent();
                break;
        }
        return true;
    }

    /**
     * 如果左右滑动距离超过一定界限，代表用户有切换菜单状态的意愿
     * 对左右划懂距离进行判断，达到一定值的时候切换菜单开关状态
     * @param scorllX chumo
     */
    private void swipeToSwitch(float scorllX) {
        Log.d(TAG, "Math.abs(event.getX()-downedX):" + Math.abs(scorllX - downedX));
        //左右滑动达到目标值
        if (scorllX - downedX >= SWITCH_DISTENCE&&currentState==MAIN_STATE) {
            currentState=MENU_STATE;
        }else if (scorllX-downedX<=-SWITCH_DISTENCE&&currentState==MENU_STATE){
            currentState=MAIN_STATE;
        }
    }

    /**
     * 根据当前的状态执行关闭/开启的f动画
     */
    private void updateCurrentContent() {
        int startX = getScrollX();
        int dx;
        //平滑滚动
        if (currentState == MENU_STATE) {//打开菜单
            //结束位置（-getChildAt(0).getMeasuredWidth()）-开始位置（startX）=平滑划动距离
            dx = -getChildAt(0).getMeasuredWidth() - startX;
        } else {//关闭菜单
            //结束位置（0）-开始位置（startX）=平滑划动距离
            dx = 0 - startX;
        }
        //动画持续时间
        int duration = Math.abs(dx);
        scroller.startScroll(startX, 0, dx, 0, duration);//自动缩回的动画
        invalidate();//最后不要忘记重绘UI！
    }

    /**
     * 得到scroller的动画位置
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {//动画还没结束
            int currX = scroller.getCurrX();
            scrollTo(currX, 0);//滚动到指定位置
            invalidate();//重绘界面
        }
    }

    /**
     * 打开侧滑菜单
     */
    public void Open() {
        currentState = MENU_STATE;
        updateCurrentContent();
    }

    /**
     * 关闭侧滑菜单
     */
    public void Close() {
        currentState = MAIN_STATE;
        updateCurrentContent();
    }

    /**
     * 获取当前侧滑菜单状态
     *
     * @return 菜单开合状态
     */
    public int getCurrentState() {
        return currentState;
    }

    /**
     * 切换侧滑菜单状态，而不需要知道当前状态
     */
    public void switchState() {
        if (currentState == MAIN_STATE) {
            Open();
        } else {
            Close();
        }
    }

    /**
     * 触摸状态的拦截判断
     *
     * @param ev 触摸动作
     * @return 是否拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offSetX = Math.abs(ev.getX() - downX);
                float offSetY = Math.abs(ev.getY() - downY);
                if (offSetX > offSetY && offSetX > INTERCEPT_DISTENCE) {//水平移动超过限定则拦截触摸
                    return true;//拦截代表由该c层View处理该触摸事件
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
