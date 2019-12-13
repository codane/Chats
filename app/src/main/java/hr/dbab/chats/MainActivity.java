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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String mUsername;

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

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //checking if user is null -> user not authenticated
        if (mFirebaseUser == null) {
            // Not signed in, launch the SignInActivity
            sendUserToSignInActivity();

        } else {
            // If the user is signed in, set the variable to the name of the user currently signed in
            mUsername = mFirebaseUser.getDisplayName();
        }
    }
    // Method which will start SignInActivity using Intent
    private void sendUserToSignInActivity(){
        Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
        // user cannot go back to MainActivity if back button pressed
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signInIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.sign_out_option){
            // signing out the user using a Firebase authentication method
            mFirebaseAuth.signOut();

            // sending the user to SignInActivity using our method
            sendUserToSignInActivity();
        }
        return true;
    }
}