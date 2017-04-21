package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DisplaySearchActivity extends AppCompatActivity {

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef = rootRef.child("hikes");

    private ArrayList<HikeListItem> allHikes = new ArrayList<HikeListItem>();
    private ListAdapter adapter;
    private Context curContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("ON CREATE", "created activity");
        curContext = this;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        allHikes = new ArrayList<HikeListItem>();

        final ListView hikeList = (ListView) findViewById(R.id.hike_list);

        childHikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    HashMap<String, Object> jsonValue = (HashMap<String, Object>)messageSnapshot.getValue();
                    String title = (String) jsonValue.get("title");
                    String difficulty = (String) jsonValue.get("difficulty");
                    String imgSrc = (String) jsonValue.get("imgSrc");
                    float distance = (float)((double)jsonValue.get("distance"));
                    float rating = (float)((double)jsonValue.get("rating"));

                    allHikes.add(new HikeListItem(imgSrc, title, HikeListItem.Difficulty.valueOf(difficulty), rating, distance));
                }

                initAdapter();
            }

            public void initAdapter()
            {
                HikeListAdapter adapter = new HikeListAdapter(curContext, allHikes.toArray(new HikeListItem[0]));
                hikeList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        allHikes.add(new HikeListItem("bishops", "Bishop Peak", HikeListItem.Difficulty.Easy, 4.5f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "Madonna", HikeListItem.Difficulty.Moderate, 4.8f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "Avila Ridge", HikeListItem.Difficulty.Hard, 4.2f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "Cabrillo", HikeListItem.Difficulty.Vigorous, 3.7f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "Cal Poly \"P\"", HikeListItem.Difficulty.Easy, 4.5f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "East Cuesta Ridge", HikeListItem.Difficulty.Moderate, 4.8f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "West Cuesta Ridge", HikeListItem.Difficulty.Hard, 4.2f, 3.3f));
//        allHikes.add(new HikeListItem("bishops", "Valencia Peak", HikeListItem.Difficulty.Vigorous, 3.7f, 3.3f));

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                //callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                Collections.shuffle(allHikes);
                HikeListAdapter adapter = new HikeListAdapter(curContext, allHikes.toArray(new HikeListItem[0]));
                hikeList.setAdapter(adapter);
            }

        });
    }

    public void getHike(View view)
    {
        //causes crash
        Intent startNewHikeActivity = new Intent(this, HikeActivity.class);
        startActivity(startNewHikeActivity);
    }
}
