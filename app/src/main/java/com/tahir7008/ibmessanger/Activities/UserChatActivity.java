package com.tahir7008.ibmessanger.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tahir7008.ibmessanger.Adapters.MessagesAdapter;
import com.tahir7008.ibmessanger.Models.Message;
import com.tahir7008.ibmessanger.R;
import com.tahir7008.ibmessanger.databinding.ActivityUserChatBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserChatActivity extends AppCompatActivity {
    ActivityUserChatBinding binding;
    MessagesAdapter myMessagesAdapter;
    ArrayList<Message> messages;

    String senderRoom;
    String receiverRoom;

    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");

        String senderUid= FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid ;
        receiverRoom = receiverUid + senderUid;

        firebaseDatabase =FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        messages = new ArrayList<Message>();
        myMessagesAdapter =new MessagesAdapter(this,messages);
        binding.rvUserChat.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUserChat.setHasFixedSize(true);
        binding.rvUserChat.setAdapter(myMessagesAdapter);

        firebaseDatabase.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 :snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                             messages.add(message);
                        }
                        myMessagesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  messageText = binding.tvTypeMsg.getText().toString();

                Date date = new Date();
                Message message = new Message(messageText,senderUid,date.getTime());
                String randomkey = firebaseDatabase.getReference().push().getKey();

                HashMap<String, Object> lastmsgObj = new HashMap<>();

                lastmsgObj.put("lastMsg",message.getMessage());
                lastmsgObj.put("lastMsgTime",date.getTime());

                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .updateChildren(lastmsgObj);

                firebaseDatabase.getReference().child("chats")
                        .child(receiverRoom)
                        .updateChildren(lastmsgObj);



                binding.tvTypeMsg.setText("");

                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(randomkey)
                            .setValue(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    firebaseDatabase.getReference().child("chats")
                                            .child(receiverRoom)
                                            .child("messages")
                                            .child(randomkey)
                                            .setValue(message)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                }
                            });

            }
        });






    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}