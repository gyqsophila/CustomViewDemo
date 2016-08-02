package com.example.alpha.customviewdemo.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;

import java.util.List;

/**
 * 自定义标签页指示器
 * Created by Alpha on 2016/8/1.
 */
public class ViewPagerIndicator extends LinearLayout {
    /**
     * 指示三角形的画笔
     */
    private Paint mPaint;
    /**
     * 图形路径
     */
    private Path mPath;
    /**
     * 指示三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 指示三角形的高度
     */
    private int mTriangleHeith;
    /**
     * 指示器三角形宽度与tab宽度的比例
     */
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6f;
    /**
     * 三角形指示器的最大宽度
     */
    private final int TRIANGLE_MAX_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH);
    /**
     * 三角形初始便宜长度
     */
    private int mInitTranslationX;
    /**
     * 三角形动态偏移长度
     */
    private int mTranslationX;
    /**
     * 页面可见tab数量
     */
    private int mVisibleTabCount;
    /**
     * 默认页面可见tab显示数量
     */
    private static final int DEFAULT_TAB_COUNT = 3;
    /**
     * 存放标题数组，用于创建tab列表
     */
    private List<String> mTitles;
    /**
     * 与指示器关联的viewpager
     */
    private ViewPager mViewPager;
    /**
     * 为外部提供的onPageChangeListener接口
     */
    private pageOnChangeListener mListener;
    /**
     * 文本非高亮颜色
     */
    private String unSelectedColor = "#77ffffff";
    /**
     * 文本高亮颜色
     */
    private String selectedColor = "#ffffff";

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleTabCount = arr.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, DEFAULT_TAB_COUNT);
        if (mVisibleTabCount <= 0) {
            mVisibleTabCount = DEFAULT_TAB_COUNT;
        }
        arr.recycle();

        initDraw();
    }

    private void initDraw() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);//填充样式
        mPaint.setPathEffect(new CornerPathEffect(3));//圆角效果
    }

    /**
     * 绘制指示器三角形
     * dispatchDraw()用与绘制viewgroup中的子view，在onDraw()之后调用
     * 当viewgroup美没有背景的时候会跳过onDraw()直接调用dispatchDraw()
     *
     * @param canvas 画布
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //暂时将原始画布数据存起来
        canvas.save();

        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        //恢复原始画布数据
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    /**
     * 重新设置指示三角形大小
     * <p/>
     * 在控件大小发生变化的时候调用（例如初始化控件）
     * onMeasured()->onlayout()
     * onSizeChanged()在onDraw()之前调用
     *
     * @param w    新的宽度
     * @param h    新的高度
     * @param oldw 旧的宽度
     * @param oldh 旧的高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) (w / mVisibleTabCount * RADIO_TRIANGLE_WIDTH);
        mTriangleWidth = Math.min(mTriangleWidth, TRIANGLE_MAX_WIDTH);
        mInitTranslationX = w / mVisibleTabCount / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    /**
     * 初始化指示三角形的数据
     */
    private void initTriangle() {
        mTriangleHeith = (int) (mTriangleWidth * RADIO_TRIANGLE_WIDTH * 2);
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeith);
        mPath.close();
    }

    /**
     * 解析xml文件后会执行的方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.weight = 0;
            params.width = getScreenWidth() / mVisibleTabCount;
            view.setLayoutParams(params);
        }
        setTabClickEvent();
    }

    /**
     * 指示器跟随手指移动
     * 根据当前指示位置和偏移量将三角形划动到新位置
     *
     * @param position 当前位置
     * @param offset   偏移量
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / mVisibleTabCount;
        mTranslationX = (int) (tabWidth * (position + offset));

        if (position >= (mVisibleTabCount - 1) && offset > 0 && getChildCount() > mVisibleTabCount) {
            if (mVisibleTabCount != 1) {
                this.scrollTo((position - (mVisibleTabCount - 1)) * tabWidth
                        + (int) (tabWidth * offset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 设置标签列表的标题
     *
     * @param titles 标题列表
     */
    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                this.addView(generateTitleView(title));
            }
            setTabClickEvent();
        }
    }

    /**
     * 根据title创建容纳tab的TextView
     *
     * @param title 标题文本
     * @return 生成的view
     */
    private View generateTitleView(String title) {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = getScreenWidth() / mVisibleTabCount;
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor(unSelectedColor));
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 设置可见tab的数量
     *
     * @param count 数量
     */
    public void setVisibleTabCount(int count) {
        this.mVisibleTabCount = count;
    }

    /**
     * 设置关联的viewPager
     *
     * @param viewPager 关联的ViewPager
     * @param mPosition 初始页面位置
     */
    public void setViewPager(ViewPager viewPager, int mPosition) {
        mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setTabHighLight(position);
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        scroll(mPosition, 0);
        setTabHighLight(mPosition);
    }

    /**
     * 因为已经使用了OnPageChangeListener这个接口，所以需要重新向外部提供一个可以使用的监听接口
     * 其中三个方法与OnPageChangeListener完全一致
     */
    public interface pageOnChangeListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    /**
     * 设置已关联页面的滚动监听接口，替代addOnPageChangeListener
     *
     * @param listener pageOnChangeListener
     */
    public void setPageOnChangeListener(pageOnChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置当前显示的tab文本高亮
     *
     * @param position 高亮的位置
     */
    private void setTabHighLight(int position) {
        for (int i = 0; i < mTitles.size(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                if (i == position) {
                    ((TextView) view).setTextColor(Color.parseColor(selectedColor));
                } else {
                    ((TextView) view).setTextColor(Color.parseColor(unSelectedColor));
                }
            }
        }
    }

    /**
     * 设置文本选中与非选中颜色
     *
     * @param selectedColor   选中颜色
     * @param unselectedColor 非选中颜色
     */
    public void setTabColor(String selectedColor, String unselectedColor) {
        if (selectedColor != null) {
            this.selectedColor = selectedColor;
        }
        if (unselectedColor != null) {
            this.unSelectedColor = unselectedColor;
        }
        invalidate();
    }

    private void setTabClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
