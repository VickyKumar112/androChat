package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    String currrentUserMobile;

    public UserRecyclerViewAdapter(Context context, ArrayList<User> userArrayList, String currrentUserMobile){
        this.context = context;
        this.userArrayList = userArrayList;
        this.currrentUserMobile = currrentUserMobile;
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.user_list_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.mobileText.setText(user.getMobile());

        holder.mobileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chat_area.class);
                intent.putExtra("mobile", user.getMobile());
                intent.putExtra("senderMobile", currrentUserMobile);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mobileText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mobileText = itemView.findViewById(R.id.listMobileNumber);
        }
    }
}
