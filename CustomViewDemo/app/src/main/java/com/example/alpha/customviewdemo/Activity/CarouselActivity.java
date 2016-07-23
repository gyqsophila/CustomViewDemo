package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片轮播
 * Created by Alpha on 2016/7/20.
 */
public class CarouselActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<ImageView> imageViewList;
    private int[] ImageResIDs;
    private String[] Descriptions;
    private int previousSelectedPotion = 0;
    boolean UiAliveed = false;


    /*@BindDrawable({R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e})
    List<Integer> ResId;*/
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.ll_point_container)
    LinearLayout llPointContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        ButterKnife.bind(this);
        viewPager.setOffscreenPageLimit(1);//设置左右缓存页面的个数
        viewPager.addOnPageChangeListener(this);
        initData();
        initAdapter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                UiAliveed=true;
                while (UiAliveed){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }).start();
    }


    private void initData() {
        ImageResIDs = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        Descriptions = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"};
        imageViewList = new ArrayList<>();
        ImageView imageView;
        View Pointview;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < ImageResIDs.length; i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(ImageResIDs[i]);
            imageViewList.add(imageView);

            //小白点
            Pointview = new View(this);
            Pointview.setBackgroundResource(R.drawable.selector_bg_point);
            params = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                params.leftMargin = 10;
            }
            Pointview.setEnabled(false);
            llPointContainer.addView(Pointview,params);
        }
    }


    private void initAdapter() {
        llPointContainer.getChildAt(0).setEnabled(true);
        tvDesc.setText(Descriptions[0]);
        previousSelectedPotion = 0;

        viewPager.setAdapter(new MyAdapter());

        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int newpos=position%imageViewList.size();
        tvDesc.setText(Descriptions[newpos]);

        //控制小白点的明暗变化
        llPointContainer.getChildAt(previousSelectedPotion).setEnabled(false);
        llPointContainer.getChildAt(newpos).setEnabled(true);

        previousSelectedPotion=newpos;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newpos=position%imageViewList.size();
            ImageView imageView=imageViewList.get(newpos);
            container.addView(imageView);
            return imageView;//返回给适配器调用
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
