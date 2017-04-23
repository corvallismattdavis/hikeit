package com.hikeit;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplaySearchActivity extends AppCompatActivity {

    ArrayList<HikeListItem> arrayList= new ArrayList<HikeListItem>();
    ListAdapter adapter;
    Context curContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        curContext = this;
        final ListView hikeList = (ListView) findViewById(R.id.hike_list);

        //Database call


        arrayList.add(new HikeListItem("bishops", "Bishop Peak", HikeListItem.Difficulty.Easy, 4.5f));
        arrayList.add(new HikeListItem("bishops", "Madonna", HikeListItem.Difficulty.Moderate, 4.8f));
        arrayList.add(new HikeListItem("bishops", "Avila Ridge", HikeListItem.Difficulty.Hard, 4.2f));
        arrayList.add(new HikeListItem("bishops", "Cabrillo", HikeListItem.Difficulty.Extreme, 3.7f));
        arrayList.add(new HikeListItem("bishops", "Cal Poly \"P\"", HikeListItem.Difficulty.Easy, 4.5f));
        arrayList.add(new HikeListItem("bishops", "East Cuesta Ridge", HikeListItem.Difficulty.Moderate, 4.8f));
        arrayList.add(new HikeListItem("bishops", "West Cuesta Ridge", HikeListItem.Difficulty.Hard, 4.2f));
        arrayList.add(new HikeListItem("bishops", "Valencia Peak", HikeListItem.Difficulty.Extreme, 3.7f));

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
                Collections.shuffle(arrayList);
                HikeListAdapter adapter = new HikeListAdapter(curContext, arrayList.toArray(new HikeListItem[0]));
                hikeList.setAdapter(adapter);
            }

        });

        HikeListAdapter adapter = new HikeListAdapter(this, arrayList.toArray(new HikeListItem[0]));
        hikeList.setAdapter(adapter);
    }


}
