package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Intent singUpIntent;
    TextView mainMsg;
    ArrayList<User> userArrayList;
    DatabaseReference databaseReference;
    String mobileNumber;
    RecyclerView recyclerView;
    UserRecyclerViewAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singUpIntent = getIntent();
        mobileNumber = singUpIntent.getStringExtra("mobile");
        userArrayList = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Users").exists()){
                    Toast.makeText(MainActivity.this, "No Any User", Toast.LENGTH_SHORT).show();
                }
                else {
                    userArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()){
                        String Mn = dataSnapshot.child("mobile").getValue(String.class);
                        if(!Mn.equals(mobileNumber)){
                            User user = dataSnapshot.getValue(User.class);
                            userArrayList.add(user);
                        }
                    }
                    callAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void callAdapter(){
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new UserRecyclerViewAdapter(MainActivity.this, userArrayList, mobileNumber);
        recyclerView.setAdapter(adapter);
    }
    long backPressTime = 0;
    @Override
    public void onBackPressed() {

        if(backPressTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this, "Back press again .", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();

    }
}