package hr.dbab.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Instantiating the toolbar and setting it as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set the title of the toolbar
        getSupportActionBar().setTitle("Register");
        // Adding back option with back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing variables
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Setting onClickListener for btnRegister
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting entered text from EditText
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Checking if the user entered everything
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                    Toast.makeText(RegisterActivity.this, "You did not enter everything", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "\"Not everything entered\"");

                }else if (password.length() < 6){

                    Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "Password does not have 6 characters");

                }else {

                    // Registering the user using our method
                    registerUser(username, email, password);
                    Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "User registered");
                }
            }
        });
    }

    private void registerUser(final String username, String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Getting the current user and storing it in a variable
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    // Storing current user's id in a variable
                    String userID = firebaseUser.getUid();

                    // Creating a new node called Users in our FirebaseDatabase and under that node we create a new user with his id
                    dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                    // Getting the data for new user to store in FirebaseDatabase
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userID);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");

                    // Saving this user data in the FirebaseDatabase
                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Cannot register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}