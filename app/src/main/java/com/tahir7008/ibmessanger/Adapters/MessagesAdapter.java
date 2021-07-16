package com.tahir7008.ibmessanger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.tahir7008.ibmessanger.Models.Message;
import com.tahir7008.ibmessanger.R;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    final  int ITEM_SENT=1;
    final  int ITEM_RECEIVE=2;

    public MessagesAdapter(Context context, ArrayList<Message> messages){
                    this.context=context;
                    this.messages =messages;
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType ==ITEM_SENT){
             View view = LayoutInflater.from(context).inflate(R.layout.item_msg_sent,parent,false);
             return  new SentViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_msg_received,parent,false);
            return  new ReceiverViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.tvSentMsg.setText(message.getMessage());

        }else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.tvReceiveMsg.setText(message.getMessage());

        }
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        TextView tvSentMsg;
        ImageView ivFeelings;
        public SentViewHolder(View itemView) {
            super(itemView);
            tvSentMsg = itemView.findViewById(R.id.tvSentMsg);
            ivFeelings =itemView.findViewById(R.id.iv_sent_feeling);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView tvReceiveMsg;
        ImageView ivFeelings;
        public ReceiverViewHolder(View itemView) {
            super(itemView);
            tvReceiveMsg = itemView.findViewById(R.id.tvRecivedMsg);
            ivFeelings = itemView.findViewById(R.id.iv_receive_feeling);
        }
    }


}
