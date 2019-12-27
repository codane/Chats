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


public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiating the toolbar and setting it as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set the title of the toolbar
        getSupportActionBar().setTitle("Login");
        // Adding back option with back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing variables
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);

        btnLogin = findViewById(R.id.btnLogin);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Setting onClickListener for btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Storing the entered text in variables
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Checking if the user entered email and password
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                    Toast.makeText(LoginActivity.this, "You did not enter everything", Toast.LENGTH_SHORT).show();
                    Log.i("LoginActivity", "Something not entered");

                }else {
                    // If the user entered email and password, he is logged in
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, SecondActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                Log.i("LoginActivity", "Login failed");
                            }
                        }
                    });
                }
            }
        });
    }

}