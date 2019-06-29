package com.findclass.renan.findclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findclass.renan.findclass.R;
import com.findclass.renan.findclass.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;
    private Context context;
    private List<Chat> mChat;
    private String imageURL;
    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> users, String imageURL) {
        this.context = context;
        this.mChat = users;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,null);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        Chat chat = mChat.get(position);

        viewHolder.show_messageTv.setText(chat.getMessage());
        if (imageURL.equals("default")){
            viewHolder.profile_imageIv.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(imageURL).into(viewHolder.profile_imageIv);
        }

        if (position == mChat.size() -1){
            if (chat.isSeen()){
                viewHolder.isSeenTv.setText(R.string.seen);
            }
            else {
                viewHolder.isSeenTv.setText(R.string.Delivered);
            }
        }
        else {
            viewHolder.isSeenTv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_messageTv, isSeenTv;
        public ImageView profile_imageIv;

        public ViewHolder(View itemView) {
            super(itemView);

            show_messageTv = itemView.findViewById(R.id.show_msg_tv);
            profile_imageIv =itemView.findViewById(R.id.profile_image);
            isSeenTv =itemView.findViewById(R.id.seen_tv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int retMsg;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid()))
        {
            retMsg = MSG_TYPE_RIGHT;
        }
        else{
            retMsg = MSG_TYPE_LEFT;
        }
        return retMsg;
    }
}
