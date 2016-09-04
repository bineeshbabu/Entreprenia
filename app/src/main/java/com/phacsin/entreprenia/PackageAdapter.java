package com.phacsin.entreprenia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GD on 9/5/2016.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    private int[] layoutList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }


    public PackageAdapter(int [] layoutList) {
        this.layoutList = layoutList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutList[viewType], parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return layoutList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}