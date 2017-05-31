package com.hikeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class BottomNavBarActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
                                                                       MapFragment.OnFragmentInteractionListener,
                                                                       AccountLoggedInFragment.OnFragmentInteractionListener,
                                                                       LoginFragment.OnFragmentInteractionListener
{

    private TextView mTextMessage;
    private Fragment curFragment;

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
                                selectedFragment = MapFragment.newInstance();
                                break;
                            case R.id.menu_search:
                                selectedFragment = SearchFragment.newInstance();
                                break;
                            case R.id.menu_settings:
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

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    public void getHike(View view)
    {
        Intent startNewHikeActivity = new Intent(this, HikeActivity.class);
//        Bundle b = new Bundle();
//        b.putString("src", allHikes.get(0).imgSrc.get(0));
//
//        startNewHikeActivity.putExtras(b);
        startActivity(startNewHikeActivity);
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
