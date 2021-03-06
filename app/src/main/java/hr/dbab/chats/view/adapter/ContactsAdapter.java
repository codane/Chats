package hr.dbab.chats.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hr.dbab.chats.view.ui.MessageActivity;
import hr.dbab.chats.R;
import hr.dbab.chats.model.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private Context mContext;
    private List<Contact> contactsList;

    public ContactsAdapter(Context mContext, List<Contact> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {

        // getting the Contact object at the certain position
        final Contact contact = contactsList.get(position);

        // setting the TextView with the name of the contact
        holder.tvContactName.setText(contact.getUsername());

        // checking if the contact has a default picture or has uploaded his desired one
        // if the picture is default, we set the ImageView with the picture from mipmap folder
        if (contact.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            // if there is a uploaded picture , we set that one to ImageView
            Glide.with(mContext).load(contact.getImageURL()).into(holder.profileImage);
        }

        // when clicked on the contact, it will send the user to MessageActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("contactid", contact.getId());
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileImage;
        private TextView tvContactName;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.contactProfileImage);
            tvContactName = itemView.findViewById(R.id.tvContactName);
        }
    }
}
