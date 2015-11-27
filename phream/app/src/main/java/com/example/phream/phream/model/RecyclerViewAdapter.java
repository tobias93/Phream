package com.example.phream.phream.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phream.phream.BitmapWorkerTask;
import com.example.phream.phream.ImageDetailView;
import com.example.phream.phream.R;


/**
 * Created by Philipp Pütz on 26.11.2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] mDataset;

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
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                        Intent intent = new Intent(context, ImageDetailView.class);
                        intent.putExtra("ImagePath", "/storage/emulated/0/Android/data/com.example.phream.phream/files/image_1448623842_2165.jpg");
                        context.startActivity(intent);
                }
            });
            mTitle =  (TextView) v.findViewById(R.id.labelTitleDescription);
            mCreateDate = (TextView) v.findViewById(R.id.labelCreateDate);
            mCreateTime = (TextView) v.findViewById(R.id.labelCreateTime);
            mImageView = (ImageView) v.findViewById(R.id.thumbnail);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset[position]);
        holder.mCreateDate.setText("26.11.2015");
        holder.mCreateTime.setText("10:45");
        BitmapWorkerTask task = new BitmapWorkerTask(holder.mImageView, 180, 180);
        task.execute("/storage/emulated/0/Android/data/com.example.phream.phream/files/image_1448623842_2165.jpg");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}