package hr.dbab.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Instantiating the toolbar and setting it as ActionBar
        Toolbar mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        // Set the title of the toolbar
        getSupportActionBar().setTitle("Chats");

        // Instantiating a ViewPager and our adapter
        ViewPager mPager = findViewById(R.id.view_pager);
        TabsAdapter mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mPager.setAdapter(mTabsAdapter);

        // Instantiating the TabLayout and setting it up with ViewPager
        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);

    }

    // Creating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    // Adding logout option in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_logout:
                // Logging out the user using Firebase method
                FirebaseAuth.getInstance().signOut();
                // sending the user to MainActivity after logging out
                startActivity(new Intent(SecondActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }

        return false;
    }
}
