package com.hikeit;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ReviewPopUp extends AppCompatActivity {
    ArrayList<Review> reviews = new ArrayList<Review>();
    HashMap<String, Integer> dbIndices = new HashMap<String, Integer>();
    String hikeTitle = "bishops";
    String hike = "Bishop Peak";

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef = rootRef.child("hikes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_pop_up);

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

        hikeTitle = getIntent().getExtras().getString("title");
        hike = getIntent().getExtras().getString("hikes");

        childHikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    HashMap<String, Object> jsonValue = (HashMap<String, Object>)messageSnapshot.getValue();
                    ArrayList<String> imgSrc = (ArrayList<String>) jsonValue.get("imgSrc");
                    if (!hikeTitle.equals(imgSrc.get(0)))
                    {
                        continue;
                    }
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
                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button submit = (Button) findViewById(R.id.submitReview);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewHike();
            }
        });
    }

    public void reviewHike()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        Log.d("TITLE", hikeTitle);
        DatabaseReference myRef = database.getReference("hikes/" +
                Integer.toString(dbIndices.get(hikeTitle)) +
                "/reviews");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        EditText review = (EditText) findViewById(R.id.userReviewInput);

        Review rev = new Review(dateFormat.format(date),
                Math.round(ratingBar.getRating()),
                auth.getCurrentUser().getEmail(),
                review.getText().toString());
        reviews.add(rev);
        ArrayList<Review> revList = new ArrayList<Review>();
        revList.add(rev);
        myRef.setValue(reviews);

        Intent backToHike = new Intent(this, HikeActivity.class);
        Bundle b = new Bundle();
        b.putString("title", hike);
        backToHike.putExtras(b);
        startActivity(backToHike);
        finish();
    }

}
