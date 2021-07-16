package com.tahir7008.ibmessanger.Adapters;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tahir7008.ibmessanger.Activities.UserChatActivity;
import com.tahir7008.ibmessanger.R;
import com.tahir7008.ibmessanger.Models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

import static java.lang.String.format;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {

    Context context;
    ArrayList <User> users;
    public UsersAdapter(Context context, ArrayList<User> users){
        this.context=context;
        this.users =users;
    }


    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  UsersAdapter.ViewHolder holder, int position) {
         User user = users.get(position);
         holder.tvUserName.setText(user.getName());

         String senderId = FirebaseAuth.getInstance().getUid();

         String senderRoom = senderId +user.getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            //long time = Long.valueOf(snapshot.child("lastMsgTime").getValue(Long.class));
                            //String dateTime = String.valueOf(format("hh:mm a",time));
                            holder.tvLastMsg.setText(lastMsg);
                            //SimpleDateFormat dateFormat = new SimpleDateFormat("");
                            //holder.tvMsgTime.setText(dateFormat.format(new Date(time)));

                        }else {
                            holder.tvLastMsg.setText("Tap To chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.ic_baseline_person_pin_24)
                .into(holder.ivUserImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivUserImage;
        TextView tvUserName;
        TextView tvLastMsg;
        TextView tvMsgTime;
        public ViewHolder( View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.ivUserProfile);
           tvUserName = itemView.findViewById(R.id.tvUserName);
           tvLastMsg = itemView.findViewById(R.id.tvLastMsg);
           tvMsgTime = itemView.findViewById(R.id.tvMsgTime);
        }

    }


}
