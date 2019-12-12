package hr.dbab.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating the toolbar and setting it as ActionBar
        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        // Set the title of the toolbar
        getSupportActionBar().setTitle("Chats");

        // Instantiating a ViewPager and our adapter
        mPager = findViewById(R.id.view_pager);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mPager.setAdapter(mTabsAdapter);

        // Instantianting the TabLayout and setting it up with ViewPager
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);
    }
}
