package com.example.phream.phream.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phream.phream.BitmapWorkerTask;
import com.example.phream.phream.R;
import com.example.phream.phream.model.Picture;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Picture[] mDataset;

    // Provides a reference to the views for each data item
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

            // find views
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

        // some simple unix->date format operations
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

        // activate context menu
        holder.mCardView.setLongClickable(true);

        // clear the image view to prevent recycled images from showing up.
        holder.mImageView.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8));
        holder.mImageView.invalidate();

        // load the image into the image view.
        BitmapWorkerTask task = new BitmapWorkerTask(holder.mImageView, mDataset[position].getFilepath(), 180, 180);
        task.execute();

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}