package hr.dbab.chats.view.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hr.dbab.chats.view.adapter.ContactsAdapter;
import hr.dbab.chats.R;
import hr.dbab.chats.model.ChatList;
import hr.dbab.chats.model.Contact;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView chatsRecycler;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList;
    private List<ChatList> usersList;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);

        chatsRecycler = view.findViewById(R.id.chatsRecycler);
        chatsRecycler.setHasFixedSize(true);
        chatsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ChatList chatList = ds.getValue(ChatList.class);
                    usersList.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void chatList() {
        contactList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Contact contact = ds.getValue(Contact.class);
                    for (ChatList chatList : usersList){
                        if (contact.getId().equals(chatList.getId())){
                            contactList.add(contact);
                        }
                    }
                }

                contactsAdapter = new ContactsAdapter(getContext(), contactList);
                chatsRecycler.setAdapter(contactsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
