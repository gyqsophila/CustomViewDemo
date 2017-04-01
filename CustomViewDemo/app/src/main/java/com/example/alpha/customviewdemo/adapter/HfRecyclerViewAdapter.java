package com.example.alpha.customviewdemo.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alpha on 2017/3/31.
 */

public class HfRecyclerViewAdapter extends RecyclerViewAdapterWrapper {
    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;

    private RecyclerView.LayoutManager layoutManager;

    private View headerView;
    private View footerview;


    public HfRecyclerViewAdapter(RecyclerView.Adapter targetAdapter) {
        super(targetAdapter);
    }


    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        getWrapperAdapter().notifyDataSetChanged();
    }

    public void removeHeaderView() {
        headerView = null;
        getWrapperAdapter().notifyDataSetChanged();
    }

    public void removeFooterView() {
        footerview = null;
        getWrapperAdapter().notifyDataSetChanged();
    }

    public void setFooterview(View footerview) {
        this.footerview = footerview;
        getWrapperAdapter().notifyDataSetChanged();
    }

    public boolean hasHeader() {
        return headerView != null;
    }

    public boolean hasFooter() {
        return footerview != null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutManager = recyclerView.getLayoutManager();
        setGridHeaderFooter(layoutManager);
    }

    private void setGridHeaderFooter(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isShowHeaderView = (position == 0 && hasHeader());
                    boolean isShowFooterView = (position == getItemCount() - 1 && hasFooter());
                    if (isShowHeaderView || isShowFooterView) {
                        return manager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasFooter() ? 1 : 0) + (hasHeader() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == 0) {
            return TYPE_HEADER;
        } else if (hasFooter() && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return super.getItemViewType(hasFooter() ? position - 1 : position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == TYPE_HEADER) {
            itemView = headerView;
        } else if (viewType == TYPE_FOOTER) {
            itemView = footerview;
        }

        if (itemView != null) {
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (params != null) {
                    layoutParams = new StaggeredGridLayoutManager
                            .LayoutParams(params.width, params.height);
                } else {
                    layoutParams = new StaggeredGridLayoutManager
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                layoutParams.setFullSpan(true);
                itemView.setLayoutParams(layoutParams);
            }
            return new RecyclerView.ViewHolder(itemView) {};
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        super.onBindViewHolder(holder, position);
    }
}
