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
import java.util.List;

/**
 * Created by Jon on 4/12/2017.
 */
class HikeListAdapter extends ArrayAdapter<HikeListItem> {

    Context context;
    final List<HikeListItem> hikes;
    private int itemResource;
    private StorageReference pictureDatabase = FirebaseStorage.getInstance().getReference();

    public HikeListAdapter(Context context, int itemResource, List<HikeListItem> data) {
        super(context, R.layout.list_row, data);
        this.context = context;
        this.hikes = data;
        this.itemResource = itemResource;
    }

    @Override
    public int getCount() {
        return hikes.size();
    }

    @Override
    public HikeListItem getItem(int position) {
        return hikes.get(position);
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

        HikeListItem hike = this.hikes.get(position);
        if (hike != null)
        {
            TextView hikeTitle = (TextView) itemView.findViewById(R.id.list_row_title);
            TextView hikeDifficulty = (TextView) itemView.findViewById(R.id.list_row_difficulty);
            TextView hikeLength = (TextView) itemView.findViewById(R.id.list_row_length);
            RatingBar hikeRating = (RatingBar) itemView.findViewById(R.id.list_row_rating);
            ImageView hikeImage = (ImageView) itemView.findViewById(R.id.list_row_img);

            hikeTitle.setText(hike.title);
            hikeDifficulty.setText(hike.difficulty.toString());
            hikeLength.setText(hike.distance + " miles");
            hikeRating.setRating(hike.rating);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 2;
            hikeImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), hike.imgResource, opt));
        }

        return itemView;
    }
}