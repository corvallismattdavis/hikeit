package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        curContext = this;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        final SearchView searchView = (SearchView) findViewById(R.id.search);
        allHikes = new ArrayList<HikeListItem>();
        final ListView hikeList = (ListView) findViewById(R.id.hike_list);

<<<<<<< HEAD
        //Database call


        arrayList.add(new HikeListItem("bishops", "Bishop Peak", HikeListItem.Difficulty.Easy, 4.5f));
        arrayList.add(new HikeListItem("bishops", "Madonna", HikeListItem.Difficulty.Moderate, 4.8f));
        arrayList.add(new HikeListItem("bishops", "Avila Ridge", HikeListItem.Difficulty.Hard, 4.2f));
        arrayList.add(new HikeListItem("bishops", "Cabrillo", HikeListItem.Difficulty.Extreme, 3.7f));
        arrayList.add(new HikeListItem("bishops", "Cal Poly \"P\"", HikeListItem.Difficulty.Easy, 4.5f));
        arrayList.add(new HikeListItem("bishops", "East Cuesta Ridge", HikeListItem.Difficulty.Moderate, 4.8f));
        arrayList.add(new HikeListItem("bishops", "West Cuesta Ridge", HikeListItem.Difficulty.Hard, 4.2f));
        arrayList.add(new HikeListItem("bishops", "Valencia Peak", HikeListItem.Difficulty.Extreme, 3.7f));
=======
        searchView.setIconified(false);
        searchView.clearFocus();

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

//                initAdapter();
            }
//
//            public void initAdapter()
//            {
//                HikeListAdapter adapter = new HikeListAdapter(curContext, allHikes.toArray(new HikeListItem[0]));
//                hikeList.setAdapter(adapter);
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
>>>>>>> 8fe1f1251340483d7e940a738b58aee8056a0c2b

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
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
        Intent startNewHikeActivity = new Intent(this, HikeActivity.class);
        startActivity(startNewHikeActivity);
    }
}
