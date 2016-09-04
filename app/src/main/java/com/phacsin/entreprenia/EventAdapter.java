package com.phacsin.entreprenia;

/**
 * Created by GD on 9/2/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phacsin.entreprenia.customfonts.MyTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Events> eventsList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,dialog_title;
        ImageView imageView;
        MyTextView details;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.event_name);
            imageView = (ImageView) view.findViewById(R.id.event_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Events event = eventsList.get(getLayoutPosition());
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    View view=inflater.inflate(R.layout.fragment_event_details,null);
                    details = (MyTextView) view.findViewById(R.id.event_brief);
                    dialog_title = (TextView) view.findViewById(R.id.event_title);
                    details.setText(event.brief);
                    dialog_title.setText(event.name);
                    MaterialDialog mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setContentView(view);
                    mMaterialDialog.setCanceledOnTouchOutside(true);
                    mMaterialDialog.show();
                }
            });
        }
    }


    public EventAdapter(List<Events> eventsList,Context context) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Events event = eventsList.get(position);
        holder.title.setText(event.name);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(event.image).resize(1280,720).onlyScaleDown().into(target);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}