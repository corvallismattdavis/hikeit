package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        ListView hikeList = (ListView) findViewById(R.id.hike_list);

//        hikeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView parentView, View childView, int position, long id)
//            {
//                getHike(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView parentView)
//            {
//            }
//        });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        allHikes = new ArrayList<HikeListItem>();

        final SearchView searchView = (SearchView) findViewById(R.id.search);
        final ListView hikeList = (ListView) findViewById(R.id.hike_list);

        searchView.setIconified(false);
        searchView.clearFocus();

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                  if (TextUtils.isEmpty(newText)) {
                     callSearch(newText);
                  }
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
        Bundle b = new Bundle();
        b.putString("src", allHikes.get(0).imgSrc.get(0));

        startNewHikeActivity.putExtras(b);
        startActivity(startNewHikeActivity);
    }
}