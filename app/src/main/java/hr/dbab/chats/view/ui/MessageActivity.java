package hr.dbab.chats.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hr.dbab.chats.R;
import hr.dbab.chats.model.Contact;
import hr.dbab.chats.model.Message;
import hr.dbab.chats.view.adapter.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView messageRecycler;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private CircleImageView messageProfileImage;
    private TextView tvMessageProfileName;
    private EditText etMessageText;
    private ImageButton btnSendMessage;

    private String contactID;

    private FirebaseUser firebaseUser;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Initializing the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, SecondActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));            }
        });

        // Initializing the RecyclerView
        messageRecycler = findViewById(R.id.messageRecycler);
        messageRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        // displaying messages from bottom
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);

        messageProfileImage = findViewById(R.id.messageProfileImage);
        tvMessageProfileName = findViewById(R.id.messageProfileName);
        etMessageText = findViewById(R.id.etMessageText);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        // we extract the data from the Intent
        Intent intent = getIntent();
        // we store that data in the variable
        contactID = intent.getStringExtra("contactid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // we send the message when this ImageButton is clicked
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // storing the text from EditText in this variable
                String msg = etMessageText.getText().toString();

                // if the variable is not empty, we send the message
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), contactID, msg);
                    Log.i("MessageActivity", "Message sent");
                }else {
                    Toast.makeText(MessageActivity.this, "You did not enter a message", Toast.LENGTH_SHORT).show();
                }
                etMessageText.setText("");
            }
        });

        // we set our DatabaseReference to the path of the certain user's id
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(contactID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);

                assert contact != null;
                tvMessageProfileName.setText(contact.getUsername());

                if (contact.getImageURL().equals("default")){
                    messageProfileImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getApplicationContext()).load(contact.getImageURL()).into(messageProfileImage);
                }

                // getting the messages between the current user and other certain user
                getMessages(firebaseUser.getUid(), contactID, contact.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Method for sending messages
    private void sendMessage(String sender, String receiver, String messageText){
        // parent node of our FirebaseDatabase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // using HashMap so we can store the data in our Database using key-value pairs
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("messageText", messageText);

        // storing data under Chats node
        databaseReference.child("Chats").push().setValue(hashMap);

        // add user to ChatsFragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(contactID);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(contactID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Method for getting messages
    private void getMessages(final String currentUserID, final String contactID, final String imageURL){
        messageList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);

                    assert message != null;
                    // if the messages are between the current user and specified user, we store them in the list
                    if (message.getReceiver().equals(currentUserID) && message.getSender().equals(contactID)
                    || message.getReceiver().equals(contactID) && message.getSender().equals(currentUserID)){
                        messageList.add(message);
                    }

                    messageAdapter = new MessageAdapter(getApplicationContext(), messageList, imageURL);
                    messageRecycler.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
