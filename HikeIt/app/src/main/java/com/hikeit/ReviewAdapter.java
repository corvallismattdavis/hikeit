package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Jon on 4/12/2017.
 */
class ReviewAdapter extends ArrayAdapter<Review> {

    Context context;
    final List<Review> reviews;
    private int itemResource;
    private StorageReference pictureDatabase = FirebaseStorage.getInstance().getReference();
    HashSet<String> users = new HashSet<String>();

    public ReviewAdapter(Context context, int itemResource, List<Review> data) {
        super(context, R.layout.list_row, data);
        this.context = context;
        this.reviews = data;
        this.itemResource = itemResource;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;

        if (convertView != null)
        {
            itemView = convertView;
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(this.itemResource, parent, false);
        }

        Review rev = this.reviews.get(position);
        if (rev != null)
        {
            if (!users.contains(rev.user))
            {
                TextView reviewUser = (TextView) itemView.findViewById(R.id.list_row_username);
                TextView reviewDate = (TextView) itemView.findViewById(R.id.list_row_date);
                TextView reviewReview = (TextView) itemView.findViewById(R.id.list_row_title);
                RatingBar reviewRating = (RatingBar) itemView.findViewById(R.id.list_row_rating);

                reviewUser.setText(rev.user);
                reviewDate.setText(rev.date);
                reviewReview.setText(rev.review);
                reviewRating.setRating(rev.rating);
                users.add(rev.user);
            }
        }

        return itemView;
    }
}