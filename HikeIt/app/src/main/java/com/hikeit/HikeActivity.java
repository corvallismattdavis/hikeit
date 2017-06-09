package com.hikeit;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.Field;
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

    private HikeListItem thisHike;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        final String hike_title = getIntent().getExtras().getString("title");

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
                    if (!hike_title.equals(title))
                    {
                        continue;
                    }
                    String difficulty = (String) jsonValue.get("difficulty");
                    ArrayList<String> imgSrc = (ArrayList<String>) jsonValue.get("imgSrc");
                    float distance = (float)((double)jsonValue.get("distance"));
                    float rating = (float)((double)jsonValue.get("rating"));
                    float lat = (float) ((double) jsonValue.get("lat"));
                    float lg = (float) ((double) jsonValue.get("lg"));
                    long numRatings = (long) jsonValue.get("numRatings");

                    HikeListItem hike = new HikeListItem(imgSrc, title, HikeListItem.Difficulty.valueOf(difficulty), rating, distance, lat, lg, numRatings);
                    int hikeImgResource = getId(hike.imgSrc.get(0), R.drawable.class);

                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 4;

                    hike.picture = BitmapFactory.decodeResource(getResources(), hikeImgResource, opt);

                    allHikes.add(hike);
                }
                initHikeActivity();
            }

            public void initHikeActivity()
            {
                thisHike = getHikeInfo();

                TextView title = (TextView) findViewById(R.id.hike_title);

                title.setText(thisHike.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("adf", "ass");
            }
        });

    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }


    //get information from bundle from the previous activity
    public HikeListItem getHikeInfo() {
        Bundle b2 = getIntent().getExtras();
        String hikeTitle = b2.getString("title");

        Log.d("Fdaf", hikeTitle);

        return getHikeFromTitle(hikeTitle);
    }

    public HikeListItem getHikeFromTitle(String title)
    {
        HikeListItem hike = null;
        Log.d("lkey", title + " " + Integer.toString(allHikes.size()));
        for (HikeListItem h : allHikes)
        {
            Log.d("GOT HIKE", h.title);
            if (h.title.equals(title))
            {
                Log.d("GOT HIKE", h.title);
                hike = h;
            }
        }

        return hike;
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
