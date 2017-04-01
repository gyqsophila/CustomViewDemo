package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.alpha.customviewdemo.R;

/**
 * 自定义的天气温度显示View
 * Created by Alpha on 2016/7/29.
 */
public class OvalDegree extends View {

    /**
     * 刻度盘文字颜色
     */
    private int text_color = getResources().getColor(R.color.colorPrimaryDark);
    /**
     * 刻度盘圆环背景色
     */
    private int ring_color = getResources().getColor(R.color.colorPrimary);
    /**
     * 中间天气状况及下方描述颜色
     */
    private int weather_color = getResources().getColor(R.color.colorAccent);

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
    private int ringWidth = getWidth() / 40;
    /**
     * 圆心横坐标
     */
    private int mCenterX;
    /**
     * 圆心纵坐标
     */
    private int mCenterY;
    /**
     * 圆环外半径
     */
    private float mRadius;
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
    private boolean drawScale = true;


    public OvalDegree(Context context) {
        super(context);
        init();
    }

    public OvalDegree(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalDegree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.OvalDegree);
        text_color = array.getColor(R.styleable.OvalDegree_weather_text_color,
                getResources().getColor(R.color.colorPrimary));
        ring_color = array.getColor(R.styleable.OvalDegree_ring_background,
                getResources().getColor(R.color.colorPrimary));
        weather_color = array.getColor(R.styleable.OvalDegree_weather_text_color,
                getResources().getColor(R.color.colorAccent));
        array.recycle();
        init();
    }


    /**
     * 初始化需要用到的三支画笔
     */
    private void init() {
        mArcPaint = new Paint();//画背景圆环
        mArcPaint.setStrokeWidth(ringWidth);//圆环宽度
        mArcPaint.setAntiAlias(true);//锯齿消除
        mArcPaint.setColor(ring_color);//背景色
        mArcPaint.setStyle(Paint.Style.STROKE);//竖线样式

        mLinepaint = new Paint();//画刻度线
        mLinepaint.setAntiAlias(true);
        mLinepaint.setColor(text_color);
        mLinepaint.setStrokeWidth(1);

        mTextPaint = new Paint();//画刻度文字
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(weather_color);
        mTextPaint.setTextSize(getWidth() / 20f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        //避免在 scrollView 里获取不到高度
        if (heightMeasureSpec == 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.AT_MOST);
        }
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, widthSpecSize);
        }
    }

    /**
     * 绘制逻辑
     *
     * @param canvas 画板
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        mCenterX = paddingLeft + width / 2;
        mCenterY = paddingTop + height / 2;
        mRadius = Math.min(width, height) / 2 - Math.min(width, height) / 5;
        //坐标指定的矩形，
        mAcrRectf = new RectF(mCenterX - mRadius, mCenterY - mRadius,
                mCenterX + mRadius, mCenterY + mRadius);
        int[] colors = {Color.BLUE, Color.GREEN, Color.RED};
        float[] positions = {0, 3f / 8, 6f / 8};
        mSweepGradient = new SweepGradient(mCenterX, mCenterY, colors, positions);

        mArcPaint.setStrokeWidth(getWidth() / 20);
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
                float top = mCenterY - mRadius - ringWidth / 2;
                //地步空白区域不画线，只旋转
                if (i <= 30 || i >= 50) {
                    //每五个刻度画一个大刻度
                    if (i % 5 == 0) {
                        top = top - getWidth() / 100;
                    }
                    canvas.drawLine(mCenterX,        //起点横坐标
                            mCenterY - mRadius, //起点纵坐标
                            mCenterX,                //终点横坐标
                            top,                    //终点纵坐标
                            mLinepaint);            //画笔
                }
                canvas.rotate(4.5f, mCenterX, mCenterY); //旋转角度、旋转中心
            }
        }
        float x = mRadius + ringWidth / 2 + getWidth() / 15; //文字中心距离圆心的距离
        float c = mRadius + ringWidth / 2 + getWidth() / 15; //斜边长度
        x = (int) Math.sqrt(x * x / 2);              //直角边长度

        canvas.rotate(135, mCenterX, mCenterY);//为了涂渐变色先转到颜色开始的角度
        canvas.drawArc(mAcrRectf, temp2degree(minTemperature),
                subTemp2degree(maxTemperature - minTemperature), false, mArcPaint);
        canvas.rotate(-135, mCenterX, mCenterY);//涂色完毕转回来

        /**
         * 七个刻度标记
         */
        canvas.drawText(startDegree + "℃", //要绘制的文字
                mCenterX - x,              //文字起点横坐标
                mCenterY + x,              //文本基线纵坐标
                mTextPaint);            //画笔
        canvas.drawText(startDegree + 10 + "℃", mCenterX - c, mCenterY + getWidth() / 75,
                mTextPaint);
        canvas.drawText(startDegree + 20 + "℃", mCenterX - x, mCenterY - x, mTextPaint);
        canvas.drawText(startDegree + 30 + "℃", mCenterX, mCenterY - c + getWidth() / 40,
                mTextPaint);
        canvas.drawText(startDegree + 40 + "℃", mCenterX + x, mCenterY - x, mTextPaint);
        canvas.drawText(startDegree + 50 + "℃", mCenterX + c, mCenterY + getWidth() / 75,
                mTextPaint);
        canvas.drawText(startDegree + 60 + "℃", mCenterX + x, mCenterY + x, mTextPaint);

        mTextPaint.setTextSize(getWidth() / 6.25f);
        canvas.drawText(getCurrentTemperature() + "℃", mCenterX,
                mCenterY + getWidth() / 20, mTextPaint);
        mTextPaint.setTextSize(getWidth() / 15f);
        canvas.drawText(getWeatherType(), mCenterX, mCenterY + c, mTextPaint);
        mTextPaint.setTextSize(getWidth() / 30f);//需要将画笔文字大小设为初始值
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
                , "The temp must in range of start degree to end degree.",
                Toast.LENGTH_SHORT).show();
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
}
