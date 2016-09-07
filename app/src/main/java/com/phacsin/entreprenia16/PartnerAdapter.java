package com.phacsin.entreprenia16;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phacsin.entreprenia16.customfonts.MyTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by GD on 9/4/2016.
 */
public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.MyViewHolder> {

    private List<String> partnerList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MyTextView details;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.partner_image);
        }
    }


    public PartnerAdapter(List<String> partnerList,Context context) {
        this.partnerList = partnerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String URL = partnerList.get(position);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.imageView.setImageResource(R.drawable.ent);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                holder.imageView.setImageResource(R.drawable.ent);

            }
        };
        Picasso.with(context).load(URL).resize(1280,720).onlyScaleDown().into(target);
    }

    @Override
    public int getItemCount() {
        return partnerList.size();
    }
}