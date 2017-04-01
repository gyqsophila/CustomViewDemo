package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.example.alpha.customviewdemo.Fragment.VPBaseFragment;
import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.UI.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用了指示器的activity
 * Created by Alpha on 2016/8/1.
 */
public class ViewPagerActivity extends FragmentActivity {
    @BindView(R.id.view_indictor)
    ViewPagerIndicator viewIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<String> mTitles;
    private List<VPBaseFragment> mContents = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        initData();
        viewIndicator.setTabColor("#ffffff","#77ffffff");
        viewIndicator.setVisibleTabCount(0);
        viewIndicator.setTabItemTitles(mTitles);
        viewPager.setAdapter(mAdapter);
        viewIndicator.setViewPager(viewPager,0);

        viewIndicator.setPageOnChangeListener(new ViewPagerIndicator.pageOnChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initData() {
        mTitles= Arrays.asList("Tab1","Tab2","Tab3","Tab4", "Tab5","Tab6","Tab7","Tab8","Tab9");
        for (String title : mTitles) {
            VPBaseFragment fragment = VPBaseFragment.newInstance(title);
            mContents.add(fragment);
        }

        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }
}
