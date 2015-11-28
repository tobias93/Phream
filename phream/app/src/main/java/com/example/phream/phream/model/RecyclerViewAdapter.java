package com.example.phream.phream.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phream.phream.BitmapWorkerTask;
import com.example.phream.phream.ImageDetailView;
import com.example.phream.phream.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Philipp Pütz on 26.11.2015.
 */
public class RecyclerViewAdapter extends ContextMenuRecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Picture[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTitle;
        public TextView mCreateDate;
        public TextView mCreateTime;
        public ImageView mImageView;
        private final Context context;

        public ViewHolder(CardView v) {
            super(v);
            context = v.getContext();
            mCardView = v;

            mTitle = (TextView) v.findViewById(R.id.labelTitleDescription);
            mCreateDate = (TextView) v.findViewById(R.id.labelCreateDate);
            mCreateTime = (TextView) v.findViewById(R.id.labelCreateTime);
            mImageView = (ImageView) v.findViewById(R.id.thumbnail);

        }

    }


    // constructor
    public RecyclerViewAdapter(Picture[] myDataset) {
        mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(mDataset[position].getName());

        Date date = new Date(mDataset[position].getCreated() * 1000L);
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = sdfdate.format(date);
        holder.mCreateDate.setText(formattedDate);

        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        String formattedTime = sdftime.format(date);
        holder.mCreateTime.setText(formattedTime);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), ImageDetailView.class);

                intent.putExtra("ImagePath", mDataset[position].getFilepath());

                holder.mCardView.getContext().startActivity(intent);
            }
        });

        holder.mCardView.setLongClickable(true);

        BitmapWorkerTask task = new BitmapWorkerTask(holder.mImageView, 180, 180);

        task.execute(mDataset[position].getFilepath());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}