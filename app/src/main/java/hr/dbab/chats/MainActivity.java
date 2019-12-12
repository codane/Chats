package hr.dbab.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating the toolbar and setting it as ActionBar
        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        // Set the title of the toolbar
        getSupportActionBar().setTitle("Chats");
    }
}
