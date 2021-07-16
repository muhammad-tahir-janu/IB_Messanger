package com.tahir7008.ibmessanger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tahir7008.ibmessanger.Models.Status;
import com.tahir7008.ibmessanger.Models.UserStatus;
import com.tahir7008.ibmessanger.R;
import com.tahir7008.ibmessanger.databinding.ItemStatusBinding;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    Context context;
    ArrayList<UserStatus> statuses;

    public StatusAdapter(Context context , ArrayList<UserStatus> statusArrayList){
            this.context =context;
            this.statuses =statusArrayList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);

        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  StatusAdapter.StatusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder{
        ItemStatusBinding binding;
        public StatusViewHolder(@NonNull  View itemView) {
            super(itemView);
            binding = ItemStatusBinding.bind(itemView);
        }
    }
}
