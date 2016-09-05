package com.phacsin.entreprenia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Bineesh P Babu on 05-09-2016.
 */
public class SampleAdapter  extends BaseExpandableListAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private final String[] mGroups = {
            "Talks",
            "Workshops",
            "Competitions",
            "Panel Discussions"
    };

    private final String[][] mChilds;

    public SampleAdapter(Context context,String[][] mChilds) {
        mContext = context;
        this.mChilds = mChilds;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mGroups.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.sample_activity_list_group_item, parent, false);
        }
/*
        final ImageView image = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
        image.setImageResource(mGroupDrawables[groupPosition]);*/

        final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);
        text.setText(mGroups[groupPosition]);

     /*   final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
        final int resId = isExpanded ? R.drawable.call : R.drawable.call;
        expandedImage.setImageResource(resId);*/

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChilds[groupPosition].length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChilds[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.sample_activity_list_child_item, parent, false);
        }

        final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_child_item_text);
        text.setText(mChilds[groupPosition][childPosition]);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
