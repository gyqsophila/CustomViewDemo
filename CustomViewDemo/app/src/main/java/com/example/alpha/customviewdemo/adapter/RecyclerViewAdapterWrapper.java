package com.example.alpha.customviewdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Alpha on 2017/3/31.
 */

public class RecyclerViewAdapterWrapper extends RecyclerView.Adapter {

    protected RecyclerView.Adapter wrapper;

    public RecyclerViewAdapterWrapper(RecyclerView.Adapter wrapper) {
        super();
        if (wrapper == null) {
            return;
        }
        this.wrapper = wrapper;
        this.wrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart,itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                notifyItemRangeChanged(positionStart,itemCount,payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart,itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
        this.wrapper = wrapper;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return wrapper.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        wrapper.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return wrapper.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return wrapper.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        wrapper.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return wrapper.getItemId(position);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return wrapper.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        wrapper.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        wrapper.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        wrapper.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        wrapper.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        wrapper.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        wrapper.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        wrapper.onDetachedFromRecyclerView(recyclerView);
    }

    public RecyclerView.Adapter getWrapperAdapter() {
        return wrapper;
    }
}
