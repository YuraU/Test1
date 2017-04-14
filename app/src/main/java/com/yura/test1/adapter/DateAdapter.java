package com.yura.test1.adapter;

import android.content.Context;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.yura.test1.R;

import java.util.ArrayList;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateAdapterViewHolder>{
    private static Context context;
    private List<String> mDates = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DateAdapter(Context context) {
        this.context = context;
    }


    @Override
    public DateAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.data_item, parent, false);
        return new DateAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateAdapterViewHolder holder, int position) {
        viewBinderHelper.bind(holder.swipeView, String.valueOf(mDates.get(position).hashCode()));
        holder.bind(mDates.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDates == null ? 0 : mDates.size();
    }

    public void addItem(String item) {
        this.mDates.add(item);
        notifyDataSetChanged();
    }

    public void setupItems(List<String> dates) {
        this.mDates.clear();
        this.mDates.addAll(dates);
        notifyDataSetChanged();
    }

    public void clear(){
        this.mDates.clear();
        notifyDataSetChanged();
    }

    public List<String> getDates() {
        return mDates;
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    public class DateAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public SwipeRevealLayout swipeView;
        public View view_bg;
        public View view_fg;

        public DateAdapterViewHolder(View view){
            super(view);
            this.swipeView = (SwipeRevealLayout)view.findViewById(R.id.swipe_layout);
            this.view_bg = view.findViewById(R.id.rl_bg);
            this.view_fg = view.findViewById(R.id.rl_fg);
            this.date = (TextView) view.findViewById(R.id.date);
        }

        public void bind(final String data, final int position) {
            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDates.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            date.setText(data);
        }
    }
}