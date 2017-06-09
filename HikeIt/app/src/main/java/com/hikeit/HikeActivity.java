package com.hikeit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import static android.R.attr.data;

public class HikeActivity extends AppCompatActivity {

    //Database Stuff
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef = rootRef.child("hikes");
    private ArrayList<HikeListItem> allHikes = new ArrayList<HikeListItem>();
    private ArrayList<Review> reviews = new ArrayList<Review>();

    HashMap<String, Integer> dbIndices = new HashMap<String, Integer>();

    private HikeListItem thisHike;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        dbIndices.put("bishop", 0);
        dbIndices.put("valencia", 1);
        dbIndices.put("madonna", 2);
        dbIndices.put("cerrocabrillo", 3);
        dbIndices.put("oatspeak", 4);
        dbIndices.put("reservoircanyon", 5);
        dbIndices.put("thep", 6);
        dbIndices.put("westcuestaridge", 7);
        dbIndices.put("johnsonranch", 8);
        dbIndices.put("terracehills", 9);
        dbIndices.put("irishhills", 10);

        final String hike_title = getIntent().getExtras().getString("title");

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
                    String description = (String) jsonValue.get("des");
                    try {
                        ArrayList<HashMap<String, Object>> revs = (ArrayList<HashMap<String, Object>>) jsonValue.get("reviews");

                        for (HashMap<String, Object> obj : revs) {
                            String date = (String) obj.get("date");
                            long rate = ((Number) obj.get("rating")).longValue();
                            String user = (String) obj.get("user");
                            String review = (String) obj.get("review");
                            Review rev = new Review(date, rate, user, review);
                            reviews.add(rev);
                        }
                    }
                    catch (ClassCastException cce)
                    {
                        Log.d("WOOPS", "when adding a review");
                    }

                    HikeListItem hike = new HikeListItem(imgSrc,
                            title,
                            HikeListItem.Difficulty.valueOf(difficulty),
                            rating,
                            distance,
                            lat,
                            lg,
                            numRatings,
                            description,
                            reviews);
                    int hikeImgResource = getId(hike.imgSrc.get(0), R.drawable.class);

                    hike.imgResource = hikeImgResource;

                    allHikes.add(hike);
                }
                initHikeActivity();
            }

            public void initHikeActivity()
            {
                thisHike = getHikeInfo();

                TextView title = (TextView) findViewById(R.id.hike_title);
                ImageView img = (ImageView) findViewById(R.id.hike_img);
                TextView des = (TextView) findViewById(R.id.hike_description);
                RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);

                title.setText(thisHike.title);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 4;
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), thisHike.imgResource, opt));
                des.setText(thisHike.description);
                rating.setRating(thisHike.rating);

                SetImageGallery();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewHike();
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

//    ImageView imageView = new ImageView(this);
//            imageView.setId(i - 1);
//            imageView.setPadding(3, 3, 3, 3);
//
//    int hikeImgResource = getId(thisHike.imgSrc.get(0) + i, R.drawable.class);
//    BitmapFactory.Options opt = new BitmapFactory.Options();
//    opt.inSampleSize = 2;
//    Bitmap pic = BitmapFactory.decodeResource(getResources(), hikeImgResource, opt);
//
//            imageView.setImageBitmap(pic);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            layout.addView(imageView);

    private void SetImageGallery()
    {
        int hikeImgResource = 0;

        ImageView image1 = (ImageView) findViewById(R.id.image1);
        ImageView image2 = (ImageView) findViewById(R.id.image2);
        ImageView image3 = (ImageView) findViewById(R.id.image3);

        hikeImgResource = getId(thisHike.imgSrc.get(0) + "1", R.drawable.class);
        Bitmap pic = BitmapFactory.decodeResource(getResources(), hikeImgResource);
        image1.setImageBitmap(pic);

        hikeImgResource = getId(thisHike.imgSrc.get(0) + "2", R.drawable.class);
        Bitmap pic2 = BitmapFactory.decodeResource(getResources(), hikeImgResource);
        image2.setImageBitmap(pic2);

        hikeImgResource = getId(thisHike.imgSrc.get(0), R.drawable.class);
        Bitmap pic3 = BitmapFactory.decodeResource(getResources(), hikeImgResource);
        image3.setImageBitmap(pic3);
    }

    private void ReviewHike() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
        {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
        else
        {
            Intent review = new Intent(this, ReviewPopUp.class);
            Bundle b = new Bundle();
            b.putString("title", thisHike.imgSrc.get(0));
            b.putString("hike", thisHike.title);
            review.putExtras(b);
            startActivity(review);
        }

        displayReviews();

    }

    private void displayReviews()
    {
        if (reviews.size() == 0)
        {
            Log.d("REVIEWS", "No reviews for this hike.");
        }
        HashSet<String> users = new HashSet<String>();
        for (Review r : reviews) {
            if (users.contains(r.user))
            {
                reviews.remove(r);
            }
            else
            {
                Log.d("REVIEW", r.toString());
                users.add(r.user);
            }
        }
    }

}
