package com.example.alpha.customviewdemo.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.adapter.HfRecyclerViewAdapter;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Random random = new Random(50);
    private CardView headerView;
    private CardView footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);
        adapter = new RecyclerView.Adapter<MyViewHolder>() {
            boolean isStaggered;

            @Override
            public void onAttachedToRecyclerView(RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
                isStaggered = recyclerView.getLayoutManager()
                        instanceof StaggeredGridLayoutManager;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CardView cardView = new CardView(RecyclerViewActivity.this);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 100);
                params.setMargins(10, 10, 10, 10);
                cardView.setLayoutParams(params);
                TextView textView = new TextView(RecyclerViewActivity.this);
                textView.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.BLACK);
                cardView.addView(textView);
                return new MyViewHolder(cardView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                int[] heights = {300, 400, 500};
                FrameLayout layout = (FrameLayout) holder.itemView;
                if (isStaggered) {
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    int height = heights[random.nextInt(3)];
                    params.height = height;
                    holder.itemView.setLayoutParams(params);
                    holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(255)
                            , random.nextInt(255), random.nextInt(255)));
                }
                ((TextView) layout.getChildAt(0)).setText("position:" + position);
            }

            @Override
            public int getItemCount() {
                return 50;
            }
        };
        initHeaderAndFooterView();
        showLineStyle();
    }

    private void initHeaderAndFooterView() {
        RecyclerView.LayoutParams params =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        params.setMargins(10, 10, 10, 10);

        headerView = new CardView(this);
        headerView.setLayoutParams(params);
        headerView.setBackgroundColor(Color.GREEN);
        TextView header = new TextView(this);
        header.setText("I am headerView");
        header.setGravity(Gravity.CENTER);
        header.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        headerView.addView(header);

        footerView = new CardView(this);
        footerView.setLayoutParams(params);
        footerView.setBackgroundColor(Color.BLACK);
        TextView footer = new TextView(this);
        footer.setText("I am footerView");
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(Color.WHITE);
        footer.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        footerView.addView(footer);
    }

    private void showLineStyle() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        HfRecyclerViewAdapter viewAdapter = new HfRecyclerViewAdapter(adapter);
        viewAdapter.setHeaderView(headerView);
        viewAdapter.setFooterview(footerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(viewAdapter);
    }

    private void showStaggerStyle() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4
                , StaggeredGridLayoutManager.VERTICAL);
        HfRecyclerViewAdapter viewAdapter = new HfRecyclerViewAdapter(adapter);
        viewAdapter.setHeaderView(headerView);
        viewAdapter.setFooterview(footerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(viewAdapter);
    }

    private void showGridStyle() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        HfRecyclerViewAdapter viewAdapter = new HfRecyclerViewAdapter(adapter);
        viewAdapter.setHeaderView(headerView);
        viewAdapter.setFooterview(footerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(viewAdapter);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_line:
                showLineStyle();
                break;
            case R.id.action_stagger:
                showStaggerStyle();
                break;
            case R.id.action_grid:
                showGridStyle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
