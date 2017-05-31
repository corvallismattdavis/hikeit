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

/**
 * Created by Jon on 4/12/2017.
 */
class HikeListAdapter extends BaseAdapter {

    Context context;
    HikeListItem[] data;
    private static LayoutInflater inflater = null;
    private StorageReference pictureDatabase = FirebaseStorage.getInstance().getReference();

    public HikeListAdapter(Context context, HikeListItem[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            holder.title = (TextView) convertView.findViewById(R.id.list_row_title);
            holder.difficulty  = (TextView) convertView.findViewById(R.id.list_row_difficulty);
            holder.distance = (TextView) convertView.findViewById(R.id.list_row_length);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_row_img);
            holder.rating = (RatingBar) convertView.findViewById(R.id.list_row_rating);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        HikeListItem hike = data[position];
        holder.title.setText(hike.title);
        holder.difficulty.setText(hike.difficulty.toString());
        holder.distance.setText(hike.distance + " miles");
        holder.rating.setRating(hike.rating);

        int id = context.getResources().getIdentifier("drawable/" + data[position].imgSrc.get(0), null, context.getPackageName());
        holder.icon.setImageResource(id);

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView difficulty;
        TextView distance;
        ImageView icon;
        RatingBar rating;
    }
}