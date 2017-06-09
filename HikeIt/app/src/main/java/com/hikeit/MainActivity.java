package com.hikeit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Begin Button Edit Methods
    public void goToSearch (View view) {
        Intent mainMenu = new Intent(this, BottomNavBarActivity.class);
        Bundle b = new Bundle();
        b.putInt("frag", 2);
        mainMenu.putExtras(b);

        startActivity(mainMenu);
    }

    public void goToAccount (View view) {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            Intent accountActivity = new Intent(this, AccountLoggedInActivity.class);
            startActivity(accountActivity);
        }
        else
        {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
        }
    }

    public void goToMaps (View view) {
        Intent maps = new Intent(this, BottomNavBarActivity.class);
        Bundle b = new Bundle();
        b.putInt("frag", 1);
        maps.putExtras(b);
        startActivity(maps);
    }





}
