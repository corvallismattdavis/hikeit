package com.hikeit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef;

    private ArrayList<HikeListItem> allHikes = new ArrayList<HikeListItem>();
    private ListAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Log.d("START", "started");
        Init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
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

    public void Init()
    {
        Log.d("INIT", "called");
        allHikes = new ArrayList<HikeListItem>();

        final SearchView searchView = (SearchView) getView().findViewById(R.id.search);
        final ListView hikeList = (ListView) getView().findViewById(R.id.hike_list);

        searchView.setIconified(false);
        searchView.clearFocus();

        childHikeRef = rootRef.child("hikes");

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
                    long numRatings = (long) jsonValue.get("numRatings");
                    String description = (String) jsonValue.get("des");
                    HikeListItem hike = new HikeListItem(imgSrc, title, HikeListItem.Difficulty.valueOf(difficulty), rating, distance, lat, lg, numRatings, description);
                    int hikeImgResource = getId(hike.imgSrc.get(0), R.drawable.class);

                    hike.imgResource = hikeImgResource;

                    allHikes.add(hike);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CANCEL", "LOAD FAILED");
            }


        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                Log.d("COMPARE", "searched");
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
                Log.d("COMPARE", "searched inside");
                final String q = query.toLowerCase().split("\\s")[0];
                if (query.contains("near") || query.contains("me"))
                {
                    Collections.sort(allHikes, HikeListItem.COMPARE_BY_CLOSE_TO_ME);
                    HikeListAdapter adapter = new HikeListAdapter(getActivity(), R.layout.list_row, allHikes);
                    hikeList.setAdapter(adapter);
                }
                else
                {
                    Comparator<HikeListItem> COMPARE_STRING = new Comparator<HikeListItem>() {
                        public int compare(HikeListItem o1, HikeListItem o2) {
                            return (getScore(o1.title).compareTo(getScore(o2.title)));
                        }

                        private Integer getScore(String s) {
                            int bestMatch = 1000000;
                            String[] tokens = s.toLowerCase().split("\\s");
                            for (int i = 0; i < tokens.length; i++)
                            {
                                int comp = tokens[i].compareTo(q);
                                if (Math.abs(comp) < bestMatch)
                                {
                                    bestMatch = Math.abs(comp);
                                }
                            }
                            return bestMatch;
                        }
                    };
                    Log.d("COMPARE", "Sorting...");
                    Collections.sort(allHikes, COMPARE_STRING);
                    HikeListAdapter adapter = new HikeListAdapter(getActivity(), R.layout.list_row, allHikes);
                    hikeList.setAdapter(adapter);
                }

                HikeListAdapter adapter = new HikeListAdapter(getActivity(), R.layout.list_row, allHikes);
                hikeList.setAdapter(adapter);
            }

        });

        hikeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView listView = (ListView) view.getParent();
                if (listView != null) {

                    HikeListItem hike = (HikeListItem)listView.getItemAtPosition(position);
                    if (hike != null) {
                            Intent startNewHikeActivity = new Intent(getActivity(), HikeActivity.class);
                            Bundle b = new Bundle();

                            //Log.d("postion", Integer.toString(position));

                            b.putString("title", hike.title);
                            //b.putFloat("");
//                            b.putString("difficulty", allHikes.get(position).difficulty.toString());
//                            b.putFloat("rating", allHikes.get(position).rating);
//                            b.putFloat("distance", allHikes.get(position).distance);
//                            b.putFloat("lat", allHikes.get(position).lat);
//                            b.putFloat("lg", allHikes.get(position).lg);

                            startNewHikeActivity.putExtras(b);
                            startActivity(startNewHikeActivity);

                    }
                }
            }
        });
    }


    public void onItemClick(AdapterView<HikeListAdapter> adapter, View v, int position, long id) {
         HikeListItem clickedHike = (HikeListItem) adapter.getItemAtPosition(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            allHikes = (ArrayList<HikeListItem>)savedInstanceState.get("allHikes");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelableArrayList("allHikes", allHikes);
    }


    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onSearchFragmentInteraction(string);
        }
    }

    public interface OnDataPass {
        public void onDataPass(ArrayList<HikeListItem> data);
    }

    OnDataPass dataPasser;


    //need either an index for which you can load information from database into hikeActivity
        //this number can be taken from a clickListener that is applied to search view


    public void passData(ArrayList<HikeListItem> allHikes) {
        dataPasser.onDataPass(allHikes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPasser = (OnDataPass) context; //prep to send allHike info


        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onSearchFragmentInteraction(String string);
    }
}
