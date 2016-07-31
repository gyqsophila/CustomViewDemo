package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * 自定义的天气温度显示View
 * Created by Alpha on 2016/7/29.
 */
public class OvalDegree extends View {

    private int defaultMinWidth = 800;
    /**
     * 用于画圆弧的笔
     */
    private Paint mArcPaint;
    /**
     * 用于画刻度线的画笔
     */
    private Paint mLinepaint;
    /**
     * 用于画文字的笔
     */
    private Paint mTextPaint;
    /**
     * 圆环宽度
     */
    private int CircleWidth = getDefaultMinWidth() / 20;
    /**
     * 圆心横纵坐标
     */
    private int mCenter;
    /**
     * 圆环外半径
     */
    private float mRadius;
    /**
     * 圆环内半径
     */
    private float insideRadius;
    /**
     * 内边距
     */
    private float insidePadding = getDefaultMinWidth() / 7.5f;
    /**
     * 由坐标指定的矩形，用于包裹整个圆环
     */
    private RectF mAcrRectf;
    /**
     * 渐变色对象
     */
    private SweepGradient mSweepGradient;
    /**
     * 刻度盘文字结束度数
     */
    private static final int endDegree = 40;
    /**
     * 刻度盘文字开始度数
     */
    private static final int startDegree = -20;
    /**
     * 最小温度，刻度盘彩色区域的起点温度
     */
    private int minTemperature = -20;
    /**
     * 最大温度，刻度盘彩色区域的终点温度
     */
    private int maxTemperature = 40;
    /**
     * 当前温度，显示在刻度盘中心
     */
    private int currentTemperature = 0;
    /**
     * 天气状况，显示在刻度盘底部
     */
    private String weatherType = "";
    /**
     * 是否画刻度线
     */
    private boolean drawScale = false;

    public OvalDegree(Context context) {
        super(context);
        init();
    }

    public OvalDegree(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OvalDegree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化需要用到的三支画笔
     */
    private void init() {
        mArcPaint = new Paint();//画背景圆环
        mArcPaint.setStrokeWidth(CircleWidth);//圆环宽度
        mArcPaint.setAntiAlias(true);//锯齿消除
        mArcPaint.setColor(Color.WHITE);//背景色
        mArcPaint.setStyle(Paint.Style.STROKE);//竖线样式

        mLinepaint = new Paint();//画刻度线
        mLinepaint.setAntiAlias(true);
        mLinepaint.setColor(Color.BLUE);
        mLinepaint.setStrokeWidth(1);

        mTextPaint = new Paint();//画刻度文字
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(getDefaultMinWidth() / 30f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 绘制逻辑
     *
     * @param canvas 画板
     */
    @Override
    protected void onDraw(Canvas canvas) {
        initDraw();
        mArcPaint.setStrokeWidth(getDefaultMinWidth() / 20);
        mArcPaint.setShader(null);//设置着色器为空，没有水平间距
        canvas.drawArc(mAcrRectf  //绘制的区域范围
                , 135             //开始角度
                , 270             //绘制的角度
                , false           //是否与圆心相连
                , mArcPaint);     //画笔
        mArcPaint.setShader(mSweepGradient);//添加渐变色
        if (isDrawScale()) {
            for (int i = 0; i < 360 / 4.5; i++) {//每隔4.5度画一条线，总共要画80条线
                //圆心整顶部直线的Y坐标
                //刻度盘刻度线长度
                float top = mCenter - mRadius - CircleWidth / 2;
                //地步空白区域不画线，只旋转
                if (i <= 30 || i >= 50) {
                    //每五个刻度画一个大刻度
                    if (i % 5 == 0) {
                        top = top - getDefaultMinWidth() / 100;
                    }
                    canvas.drawLine(mCenter,        //起点横坐标
                            mCenter - mRadius, //起点纵坐标
                            mCenter,                //终点横坐标
                            top,                    //终点纵坐标
                            mLinepaint);            //画笔
                }
                canvas.rotate(4.5f, mCenter, mCenter); //旋转角度、旋转中心
            }
        }
        float x = mRadius + CircleWidth / 2 + getDefaultMinWidth() / 15; //文字中心距离圆心的距离
        float c = mRadius + CircleWidth / 2 + getDefaultMinWidth() / 15; //斜边长度
        x = (int) Math.sqrt(x * x / 2);              //直角边长度

        canvas.rotate(135, mCenter, mCenter);//为了涂渐变色先转到颜色开始的角度
        canvas.drawArc(mAcrRectf, temp2degree(minTemperature), subTemp2degree(maxTemperature - minTemperature), false, mArcPaint);
        canvas.rotate(-135, mCenter, mCenter);//涂色完毕转回来

        /**
         * 七个刻度标记
         */
        canvas.drawText(startDegree + "℃", //要绘制的文字
                mCenter - x,              //文字起点横坐标
                mCenter + x,              //文本基线总表做
                mTextPaint);            //画笔
        canvas.drawText(startDegree + 10 + "℃", mCenter - c, mCenter + getDefaultMinWidth() / 75, mTextPaint);
        canvas.drawText(startDegree + 20 + "℃", mCenter - x, mCenter - x, mTextPaint);
        canvas.drawText(startDegree + 30 + "℃", mCenter, mCenter - c + getDefaultMinWidth() / 25, mTextPaint);
        canvas.drawText(startDegree + 40 + "℃", mCenter + x, mCenter - x, mTextPaint);
        canvas.drawText(startDegree + 50 + "℃", mCenter + c, mCenter + getDefaultMinWidth() / 75, mTextPaint);
        canvas.drawText(startDegree + 60 + "℃", mCenter + x, mCenter + x, mTextPaint);

        mTextPaint.setTextSize(getDefaultMinWidth() / 6.25f);
        canvas.drawText(getCurrentTemperature() + "℃", mCenter, mCenter + getDefaultMinWidth() / 20, mTextPaint);
        mTextPaint.setTextSize(getDefaultMinWidth() / 15f);
        canvas.drawText(getWeatherType(), mCenter, mCenter + c, mTextPaint);
        mTextPaint.setTextSize(getDefaultMinWidth() / 30f);//需要将画笔文字大小设为初始值
    }

    /**
     * 初始化绘制数据
     */
    private void initDraw() {
        mCenter = getDefaultMinWidth() / 2;
        mRadius = getDefaultMinWidth() / 2 - insidePadding;
        insideRadius = mRadius - CircleWidth / 2;//内半径=半径-圆环宽度的一半
        //坐标指定的矩形，
        mAcrRectf = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter + mRadius, mCenter + mRadius);
        int[] colors = {Color.BLUE, Color.GREEN, Color.RED};
        float[] positions = {0, 3f / 8, 6f / 8};
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, positions);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        CircleWidth = getDefaultMinWidth() / 20;
    }

    /**
     * 测量实际宽度
     *
     * @param measureSpec 待确认宽度
     * @return 实际宽度
     */
    private int measureWidth(int measureSpec) {
        int result = getDefaultMinWidth();
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {//精确值模式
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {//模糊模式
            result = Math.min(result, size);
        }
        setDefaultMinWidth(result);
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = getDefaultMinWidth();
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(result, size);
        }
        return Math.min(result, getDefaultMinWidth());
    }

    /**
     * 根据温度得到圆环中的角度位置
     *
     * @param temperature 温度
     * @return 在圆环中的角度
     */
    private float temp2degree(int temperature) {
        return (float) (((temperature - (-20)) * 4.5));
    }

    /**
     * 根据温度差计算需要旋转的角度
     *
     * @param temperatureDifference 温度差
     * @return 旋转角度
     */
    private float subTemp2degree(int temperatureDifference) {
        return (float) (temperatureDifference * 4.5);
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        if (minTemperature >= startDegree && minTemperature <= maxTemperature) {
            this.minTemperature = minTemperature;
            invalidate();
        } else {
            setRangeError();
        }
    }

    /**
     * 设置温度范围出错时调用
     */
    private void setRangeError() {
        Toast.makeText(getContext()
                , "The temp must in range of start degree to end degree.", Toast.LENGTH_SHORT).show();
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * 设置当天最高温度
     *
     * @param maxTemperature 最高温度
     */
    public void setMaxTemperature(int maxTemperature) {
        if (maxTemperature <= endDegree && maxTemperature >= minTemperature) {
            this.maxTemperature = maxTemperature;
            invalidate();
        } else {
            setRangeError();
        }
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    /**
     * 设置当前温度
     *
     * @param currentTemperature 当前温度
     */
    public void setCurrentTemperature(int currentTemperature) {
        if (currentTemperature >= minTemperature && currentTemperature <= maxTemperature) {
            this.currentTemperature = currentTemperature;
        } else {
            setError();
        }
    }

    public String getWeatherType() {
        return weatherType;
    }

    /**
     * 设置天气状况
     *
     * @param weatherType 天气状况信息
     */
    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    /**
     * 当设置信息出错的时候调用
     */
    private void setError() {
        Toast.makeText(getContext(), "Valus must within this range of "
                + minTemperature + " to " + maxTemperature + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否画刻度线
     */
    public boolean isDrawScale() {
        return drawScale;
    }

    /**
     * 是否画刻度线
     *
     * @param drawScale true or fals
     */
    public void setDrawScale(boolean drawScale) {
        this.drawScale = drawScale;
        invalidate();
    }

    /**
     * 默认最小宽度和高度
     */
    public int getDefaultMinWidth() {
        return defaultMinWidth;
    }

    /**
     * 设置view高度和宽度
     *
     * @param defaultMinWidth 高度和宽度
     */
    public void setDefaultMinWidth(int defaultMinWidth) {
        this.defaultMinWidth = defaultMinWidth;
    }
}
