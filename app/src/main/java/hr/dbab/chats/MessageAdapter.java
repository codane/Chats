package hr.dbab.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int TYPE_MESSAGE_LEFT = 0;
    public static final int TYPE_MESSAGE_RIGHT = 1;
    private Context mContext;
    private List<Message> messageList;
    private String imageURL;

    public MessageAdapter(Context mContext, List<Message> messageList, String imageURL) {
        this.mContext = mContext;
        this.messageList = messageList;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // we inflate this layout if the sender is the current user
        if (viewType == TYPE_MESSAGE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_right, parent, false);
            return new MessageViewHolder(view);
        }else {
            // if the sender is not the current user, inflate this layout
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_left, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // setting the TextView with the message text
        holder.tvShowMessage.setText(message.getMessageText());


        if (imageURL.equals("default")){
            holder.messageProfileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageURL).into(holder.messageProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView messageProfileImage;
        private TextView tvShowMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageProfileImage = itemView.findViewById(R.id.messageProfileImage);
            tvShowMessage = itemView.findViewById(R.id.tvShowMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // getting current user so that we can access it's id
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // checking if the sender of the message at a certain position is current user
        if (messageList.get(position).getSender().equals(firebaseUser.getUid())){
            // if it is, we will use this constant to inflate a view which will display that message
            // in the right part of the screen
            return TYPE_MESSAGE_RIGHT;
        }else {
            return TYPE_MESSAGE_LEFT;
        }
    }
}
