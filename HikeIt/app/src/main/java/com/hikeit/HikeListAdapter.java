package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.net.Uri;
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
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        View vi = convertView;

        if (vi == null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView) vi.findViewById(R.id.list_row_title);
        title.setText(data[position].title);

        TextView difficulty = (TextView) vi.findViewById(R.id.list_row_difficulty);
        difficulty.setText(data[position].difficulty.toString());

        TextView distance = (TextView) vi.findViewById(R.id.list_row_length);
        distance.setText((data[position].distance + " miles"));

        final ImageView icon = (ImageView) vi.findViewById(R.id.list_row_img);

//        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hikeit-c31a4.appspot.com/");
//        ref.child("images/" + data[position].imgSrc + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("FILE DOWNLOAD", "Successfully loaded uri");
//                icon.setImageURI(uri);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("FILE DOWNLOAD", "Failed loading uri: " + e.getMessage());
//                icon.setImageResource(R.drawable.empty_photo);
//            }
//        });

//        vi.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        RatingBar rating = (RatingBar) vi.findViewById(R.id.list_row_rating);
        rating.setRating(data[position].rating);
        return vi;
    }
}
