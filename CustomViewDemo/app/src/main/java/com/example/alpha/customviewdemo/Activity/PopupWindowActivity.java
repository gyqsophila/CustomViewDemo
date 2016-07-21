package com.example.alpha.customviewdemo.Activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 弹出选择窗口
 * Created by Alpha on 2016/7/21.
 */
public class PopupWindowActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<String> datas;
    private PopupWindow popupWindow;

    @BindView(R.id.input_text)
    EditText input_text;
    @BindView(R.id.dropdown_button)
    ImageButton dropdownButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.dropdown_button)
    public void onClick() {
        showPopupWindow();
    }

    private void showPopupWindow() {
        initListView();

        popupWindow=new PopupWindow(
                listView,//弹出窗口的内容
                input_text.getWidth(),//窗口宽度
                600);//窗口高度
        popupWindow.setOutsideTouchable(true);//窗口以外区域可点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//添加背景用于响应点击事件
        popupWindow.setFocusable(true);//可获取焦点
        popupWindow.showAsDropDown(input_text,//显示在哪个控件下方
                0,//水平偏移像素
                -5);//竖直便宜像素
    }

    private void initListView() {
        listView=new ListView(this);
        listView.setDividerHeight(0);//每个item之间的分割线高度
        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setOnItemClickListener(this);
        datas=new ArrayList<>();
        for (int i=0;i<20;i++){
            datas.add((10000+i)+"");
        }
        listView.setAdapter(new MyAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        input_text.setText(datas.get(position));
        popupWindow.dismiss();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView==null){
                view=View.inflate(parent.getContext(),R.layout.drop_item_layout,null);
            }else {
                view=convertView;
            }

            TextView tv_number= (TextView) view.findViewById(R.id.tv_number);
            tv_number.setText(datas.get(position));

            view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    notifyDataSetChanged();
                    if (datas.size()==0){
                        popupWindow.dismiss();
                    }
                }
            });

            return view;
        }
    }
}
