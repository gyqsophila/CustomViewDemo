package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关按钮
 * Created by Alpha on 2016/7/21.
 */
public class ToggleButton extends View {

    /**
     * view背景
     */
    private Bitmap switchBackgroundBitmap;
    /**
     * 滑块背景
     */
    private Bitmap slideButtonBitmap;
    /**
     * 用于绘制的画笔
     */
    private Paint paint;
    /**
     * 记录开关状态
     */
    private boolean mSwitchState = true;
    /**
     * 标记当前是否为触摸模式
     */
    private boolean isTouchMode = false;
    /**
     * 记录当前触摸点的x坐标的值
     */
    private float currentX;
    /**
     * 开关状态监听接口
     */
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener;

    /**
     * 用于代码创建控件
     *
     * @param context
     */
    public ToggleButton(Context context) {
        super(context);
        initial();
    }

    /**
     * 用于在XML里创建，可以自定义属性
     *
     * @param context
     * @param attrs
     */
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();

        String namespace = "http://schemas.android.com/apk/res-auto";
        int switchBackgroundResource = attrs.getAttributeResourceValue(
                namespace,//自定义命名空间
                "switch_background",//自定义属性的标识符
                -1);//默认值
        int slideButtonResource = attrs.getAttributeResourceValue(
                namespace,
                "slide_button",
                -1);

        mSwitchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);
        setSwitchBackgroundResource(switchBackgroundResource);//设置背景图
        setSlideButtonResource(slideButtonResource);//设置滑块图片
    }

    /**
     * 用于在XML里创建，可以自定义属性和样式
     *
     * @param context      上下文
     * @param attrs        参数
     * @param defStyleAttr 样式
     */
    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }

    /**
     * 初始化画笔
     */
    private void initial() {
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量初始控件的大小，以view背景图大小为准
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1. 绘制背景
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);

        //2. 根据用户的触摸位置绘制滑块
        //2.1 如果是触摸状态
        if (isTouchMode) {
            //为了使滑块以点击坐标为横轴中心，需要要将滑块的横坐标缩小为滑块宽度的1/2
            float newleft = currentX - slideButtonBitmap.getWidth() / 2.0f;
            //滑块能带到达的最右边的位置，防止划出适当的范围
            int maxLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();

            //限制滑块的左右位置
            if (newleft < 0) {
                newleft = 0;
            } else if (newleft > maxLeft) {
                newleft = maxLeft;
            }
            //实际绘制滑块
            canvas.drawBitmap(slideButtonBitmap, newleft, 0, paint);
        } else {//不是触摸划动状态，根据开关状态绘制
            if (mSwitchState) {
                //开，滑块在最右边
                int newleft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
                canvas.drawBitmap(slideButtonBitmap, newleft, 0, paint);
            } else {
                //关，滑块在最左边
                canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
            }
        }
    }

    /**
     * 重写触摸事件，响应用户触摸
     *
     * @param event 触摸项
     * @return 是否消费该触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                isTouchMode = true;
                //记录横坐标
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE://滑动
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP://抬起
                isTouchMode = false;
                currentX = event.getX();

                float center = switchBackgroundBitmap.getWidth() / 2.0f;
                boolean state=currentX>center;
                //根据抬起的位置判断开关状态,并传递给UI界面
                if (state!=mSwitchState&&onSwitchStateUpdateListener!= null){
                    onSwitchStateUpdateListener.onStateUpdate(state);
                }
                mSwitchState=state;//同步更新
                break;
        }
        //每次的触摸都需要UI的反馈，具体表现就是UI的重绘
        invalidate();
        return true;
    }


    /**
     * 设置整个view的背景
     *
     * @param switchBackgroundResource 背景id
     */
    private void setSwitchBackgroundResource(int switchBackgroundResource) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switchBackgroundResource);
    }

    /**
     * 设置滑块背景
     *
     * @param slideButtonResource 滑块背景id
     */
    private void setSlideButtonResource(int slideButtonResource) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButtonResource);
    }

    /**
     * 为外部提供设置开关状态
     * @param mSwitchState
     */
    public void setSwitchState(boolean mSwitchState){
        this.mSwitchState=mSwitchState;
    }

    public interface OnSwitchStateUpdateListener {
        //状态回调，传递开关状态
        void onStateUpdate(boolean state);
    }

    /**
     * 设置监听接口
     * @param onSwitchStateUpdateListener 传入的接口实例
     */
    public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener){
        this.onSwitchStateUpdateListener=onSwitchStateUpdateListener;
    }
}
