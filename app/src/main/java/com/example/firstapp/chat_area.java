package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chat_area extends AppCompatActivity {
    EditText message;
    ImageView msgSendBtn;
    TextView userMobileNumber;
    Intent intent;
    DatabaseReference databaseReference;
    ArrayList<Message> messageArrayList;

    RecyclerView recyclerView;
    chatAdapter adapter;
    String senderMobile;
    String reciverMobile;
    TextView errorMsg;

    private static final String CHANNEL_ID = "Msg Channel";
    private static final int NOTIFICATION_ID = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        message = findViewById(R.id.messageBox);
        msgSendBtn = findViewById(R.id.msgSendBtn);
        intent = getIntent();

        userMobileNumber = findViewById(R.id.userMobileNumber);
        messageArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        senderMobile = intent.getStringExtra("senderMobile");
        reciverMobile = intent.getStringExtra("mobile");
//        errorMsg = findViewById(R.id.errorMsg);
        userMobileNumber.setText(reciverMobile);

        msgSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = message.getText().toString().trim();
                String msgId = new Date().getTime()+"";
                if(!messageText.isEmpty()) {
                    Message msg = new Message(messageText, senderMobile, reciverMobile, msgId);
                    databaseReference.child("Message").child(msgId).setValue(msg);
                    message.setText("");
                    messageArrayList.add(msg);
                }
                else{
                    Toast.makeText(chat_area.this, "Empty Message can't be send", Toast.LENGTH_SHORT).show();
                }
                callAdapter();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Message").getChildren()){
                    Message msgText = dataSnapshot.getValue(Message.class);
                    String reci = msgText.getReMobile();
                    String sender = msgText.getSenderMobile();
                    if((reci.equals(reciverMobile) && sender.equals(senderMobile)) || (sender.equals(reciverMobile) && reci.equals(senderMobile))) {
                        messageArrayList.add(msgText);
//                        sendNotification(msgText.getMessage(), msgText.getReMobile());
                    }
                }
                callAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    public void sendNotification(String msg, String mobileNo){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentText(msg)
                    .setSubText(mobileNo)
                    .setChannelId(CHANNEL_ID)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "MSG Channel", NotificationManager.IMPORTANCE_HIGH));
        }
        else{
            notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentText(msg)
                    .setSubText(mobileNo)
                    .build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
    public void callAdapter(){
        recyclerView = findViewById(R.id.msgRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new chatAdapter(chat_area.this, messageArrayList, senderMobile);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }
}