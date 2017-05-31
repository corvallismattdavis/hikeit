package com.hikeit;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import static android.R.attr.data;

public class HikeActivity extends AppCompatActivity {

    //Database Stuff
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef = rootRef.child("hikes");
    private ArrayList<HikeListItem> allHikes = new ArrayList<HikeListItem>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        loadImage();
        SetImageGallery();


        //START FOR DATABASE STUFF
            allHikes = new ArrayList<HikeListItem>();

            childHikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        HashMap<String, Object> jsonValue = (HashMap<String, Object>)messageSnapshot.getValue();
                        String title = (String) jsonValue.get("title");
                        String difficulty = (String) jsonValue.get("difficulty");
                        ArrayList<String> imgSrc = (ArrayList<String>) jsonValue.get("imgSrc");
                        float distance = (float)((double)jsonValue.get("distance"));
                        float rating = (float)((double)jsonValue.get("rating"));
                        float lat = (float) ((double) jsonValue.get("lat"));
                        float lg = (float) ((double) jsonValue.get("lg"));

                        allHikes.add(new HikeListItem(imgSrc, title, HikeListItem.Difficulty.valueOf(difficulty), rating, distance, lat, lg));
                    }

                    initHikeActivity();
                }

                public void initHikeActivity() {

                    getHikeInfo();
//                    for (int i = 0; i < allHikes.size(); i++) {
//
////                        float latLoop = allHikes.get(i).lat;
////                        float longLoop = allHikes.get(i).lg;
//
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    //get information from bundle from the previous activity
    public void getHikeInfo() {
        Bundle b2 = new Bundle();
        b2.getString("src",  allHikes.get(0).title);
    }

    private void loadImage()
    {
        Bundle b = getIntent().getExtras();
        String src = "empty";
        if (b != null)
        {
            src = b.getString("src");
        }

        //new DownloadImageTask((ImageView) findViewById(R.id.hike_img)).execute(src);
    }

    private void SetImageGallery()
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(3, 3, 3, 3);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.bishops));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }
    }

    private void ReviewHike() {

        //Write a review about this hike:
            //prompt for a star rating
            //go into database (incremenet counter for number of reviews)
            //update the rating based on (+= review score / number of reviews)

        //Add text to your review:
            //create string buffer that will create a variable in the database holding the sentence
            //display the reviews in a separate section
            //

    }

}
