package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference childHikeRef = rootRef.child("hikes");

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Init();
    }

    public void getHike(View view)
    {
        Intent startNewHikeActivity = new Intent(getActivity(), HikeActivity.class);
        Bundle b = new Bundle();
        b.putString("src", allHikes.get(0).imgSrc.get(0));

        startNewHikeActivity.putExtras(b);
        startActivity(startNewHikeActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void Init()
    {
        allHikes = new ArrayList<HikeListItem>();

        final SearchView searchView = (SearchView) getView().findViewById(R.id.search);
        final ListView hikeList = (ListView) getView().findViewById(R.id.hike_list);

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
                if (query.contains("near") || query.contains("me"))
                {
                    Collections.sort(allHikes, HikeListItem.COMPARE_BY_CLOSE_TO_ME);
                }
                else
                {
                    final Set<String> matches = new HashSet<String>();
                    for(String tokens : query.split("\\s")) {
                        matches.add(tokens.toLowerCase()); //convert the search string into tokens
                    }
                    Comparator<HikeListItem> COMPARE_STRING = new Comparator<HikeListItem>() {
                        public int compare(HikeListItem o1, HikeListItem o2) {
                            int scoreDiff = getScore(o1.title) - getScore(o2.title);
                            if((getScore(o1.title) == 0 && getScore(o2.title) == 0) || scoreDiff == 0) {
                                return o1.compareTo(o2);
                            }
                            return - (getScore(o1.title) - getScore(o2.title));
                        }

                        private int getScore(String s) {
                            int score = 0;
                            for(String match : matches) {
                                if(s.toLowerCase().contains(match)) {
                                    score++;
                                }
                            }
                            return score;
                        }
                    };
                    Collections.sort(allHikes, COMPARE_STRING);
                }

                HikeListAdapter adapter = new HikeListAdapter(getActivity(), allHikes.toArray(new HikeListItem[0]));
                hikeList.setAdapter(adapter);
            }


        });
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
