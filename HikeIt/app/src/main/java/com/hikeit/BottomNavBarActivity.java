package com.hikeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BottomNavBarActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
                                                                       MapFragment.OnFragmentInteractionListener,
                                                                       AccountLoggedInFragment.OnFragmentInteractionListener,
                                                                        SearchFragment.OnDataPass,
                                                                       LoginFragment.OnFragmentInteractionListener
{

    private TextView mTextMessage;
    private Fragment curFragment;

    Fragment mapsFrag = null;
    Fragment searchFrag = null;
    Fragment profFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            //Restore the fragment's instance
//            curFragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
//        }
        setContentView(R.layout.activity_bottom_nav_bar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                                    @Override
                                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                        Fragment selectedFragment = null;
                                        switch (item.getItemId()) {
                                            case R.id.menu_favorites:
                                                if (mapsFrag == null) {
                                                    selectedFragment = MapFragment.newInstance();
                                                    mapsFrag = selectedFragment;
                                                }
                                                else
                                                {
                                                    selectedFragment = mapsFrag;
                                                }
                                                break;
                                            case R.id.menu_search:
                                                if (searchFrag == null) {
                                                    selectedFragment = SearchFragment.newInstance();
                                                    searchFrag = selectedFragment;
                                                }
                                                else
                                                {
                                                    selectedFragment = searchFrag;
                                                }
                                                break;
                                            case R.id.menu_settings:
//                                                if (profFrag ==null) {
//
//                                                }
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                if (auth.getCurrentUser() != null)
                                {
                                    selectedFragment = AccountLoggedInFragment.newInstance();
                                }
                                else
                                {
                                    selectedFragment = LoginFragment.newInstance();
                                }
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        int defaultFrag = getIntent().getExtras().getInt("frag");
        if (defaultFrag == 1)
        {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, MapFragment.newInstance());
            transaction.commit();
        }
        else {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
            transaction.commit();
        }

        //Used to select an item programmatically
        bottomNavigationView.getMenu().getItem(defaultFrag - 1).setChecked(true);
    }




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }


    //get info from SearchFragment for HikeActivity Page
    @Override
    public void onDataPass(ArrayList<HikeListItem> allHikes) {
        Log.d("LOG","hello " + allHikes.get(0).title);
    }

    public void signoutClicked (View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSearchFragmentInteraction(String string)
    {

    }

    @Override
    public void onMapsFragmentInteraction(String string)
    {

    }

    @Override
    public void onAccountLoggedInFragmentInteraction(String string)
    {

    }

    @Override
    public void onLoginFragmentInteraction(String string)
    {

    }

}
