package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.UI.RefeashListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下拉刷新demo
 * Created by Alpha on 2016/7/23.
 */
public class RefeathActivity extends AppCompatActivity {

    @BindView(R.id.refeash_list_view)
    RefeashListView refeashListView;

    private List<String> datalist;
    private myAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_refeash);
        ButterKnife.bind(this);

        refeashListView.setOnRefeashListener(new RefeashListView.onRefreshListener() {
            @Override
            public void onRefeash() {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        datalist.add(0,"我是下拉刷新出来的数据");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                refeashListView.onRefeashComplete();
                            }
                        });
                    }
                }.start();
            }

        });
        datalist =new ArrayList<>();
        for (int i=0;i<30;i++){
            datalist.add("这是一条原始的listview数据："+i);
        }

        adapter = new myAdapter();
        refeashListView.setAdapter(adapter);

    }

    private class myAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(datalist.get(position));
            textView.setTextSize(18f);
            textView.setPadding(10,10,10,10);
            return textView;
        }
    }
}
