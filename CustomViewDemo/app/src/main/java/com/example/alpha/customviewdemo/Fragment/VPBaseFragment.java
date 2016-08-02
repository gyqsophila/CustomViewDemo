package com.example.alpha.customviewdemo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * viewpager里放的fragment
 * Created by Alpha on 2016/8/1.
 */
public class VPBaseFragment extends Fragment {

    private String mTitle;
    public static final String BUNDLE_TITLE = "title";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(BUNDLE_TITLE);
        }

        TextView tv = new TextView(getActivity());
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    /**
     * 根据string创建新的fragment
     *
     * @param title 标题
     * @return 新的fragment
     */
    public static VPBaseFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);

        VPBaseFragment fragment = new VPBaseFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
