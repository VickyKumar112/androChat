package com.example.firstapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TintableCheckedTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Message> messageArrayList;
    String currentMobile;
    private static int SEND_TEXT = 1;
    private static int RECIEVE_TEXT = 2;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public chatAdapter(Context context, ArrayList<Message> messageArrayList, String currentMobile) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.currentMobile = currentMobile;
    }

    @Override
    public int getItemViewType(int position) {
        if(messageArrayList.get(position).getSenderMobile().equals(currentMobile)) return SEND_TEXT;
        else return RECIEVE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == SEND_TEXT) {
            view = LayoutInflater.from(context).inflate(R.layout.send_message, parent, false);
            return new senderViewHolder(view);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.recieve_message, parent, false);
            return new recieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == SEND_TEXT) {
            ((senderViewHolder) holder).setTextMsg(messageArrayList.get(position));
        }
        else {
            ((recieverViewHolder) holder).setTextMsg(messageArrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class senderViewHolder extends RecyclerView.ViewHolder{
        TextView sendText;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendText = itemView.findViewById(R.id.sendMsg);
        }
        public void setTextMsg(Message message){
            sendText.setText(message.getMessage());
            sendText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteMsg(message);
                    return true;
                }
            });
        }
    }

    class recieverViewHolder extends RecyclerView.ViewHolder{
        TextView recieveText;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieveText = itemView.findViewById(R.id.recieveMsg);
        }
        public void setTextMsg(Message message){
            recieveText.setText(message.getMessage());
        }
    }

    public void deleteMsg(Message message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Delete ?")
                .setIcon(R.drawable.baseline_delete_24)
                .setMessage("Do you want to delete message.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msgId = message.getMsgId();
                        databaseReference.child("Message").child(msgId).removeValue();
                        Toast.makeText(context, "Message deleted.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Message not deleted. ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
