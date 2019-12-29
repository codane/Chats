package hr.dbab.chats;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView contactsRecycler;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactsList;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);

        // Initializing the RecyclerView
        contactsRecycler = view.findViewById(R.id.contactsRecycler);
        contactsRecycler.setHasFixedSize(true);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiting the variable
        contactsList = new ArrayList<>();

        // Calling our method to get the contacts
        getContacts();

        return view;

    }

    // Method to retrieve the contacts
    private void getContacts(){
        // Getting the current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Creating a DatabaseReference to get the data under the Users node in our FirebaseDatabase
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactsList.clear();
                // Looping through all the contacts under the Users node
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Contact contact = ds.getValue(Contact.class);

                    // Adding all the contacts in our list except the current user
                    assert contact != null;
                    assert firebaseUser != null;
                    if (!contact.getId().equals(firebaseUser.getUid())){
                        contactsList.add(contact);
                    }
                }

                // Creating a new Adapter instance and setting our RecyclerView to adapter
                contactsAdapter = new ContactsAdapter(getContext(), contactsList);
                contactsRecycler.setAdapter(contactsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
