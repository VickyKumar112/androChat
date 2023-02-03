package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class singup extends AppCompatActivity {

    AppCompatButton userSingUpBtn;
    EditText userName;
    EditText userEmail;
    EditText userMobile;
    TextView warningMsg;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        try{
            InputStream file = openFileInput("root.txt");
            if(file != null){
                InputStreamReader inputStreamReader = new InputStreamReader(file);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String output = "", output1 = "";
                try{
                    while ((output1 = bufferedReader.readLine()) != null){
                        output += output1;
                    }
                }
                catch (Exception e){

                }
                callIntent(output);
            }
        }
        catch (Exception e){

        }


        databaseReference = FirebaseDatabase.getInstance().getReference();
        warningMsg = findViewById(R.id.warningMsg);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userMobile = findViewById(R.id.userMobile);
        userSingUpBtn = findViewById(R.id.userSingUpBtn);
        userSingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = userName.getText().toString().trim();
                String emailText = userEmail.getText().toString().trim();
                String mobileText = userMobile.getText().toString().trim();

                if(nameText.isEmpty() || emailText.isEmpty() || mobileText.isEmpty()){
                    Toast.makeText(singup.this, "Fill All Required Field .", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("Users").hasChild(mobileText)){
                                Toast.makeText(singup.this, "Mobile Number Already Exist.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                User user = new User(nameText, mobileText, emailText);
                                databaseReference.child("Users").child(mobileText).setValue(user);
                                Toast.makeText(singup.this, "User Created", Toast.LENGTH_SHORT).show();
                                try {
                                    FileOutputStream file = openFileOutput("root.txt", MODE_PRIVATE);
                                    OutputStreamWriter outputWrite = new OutputStreamWriter(file);
                                    outputWrite.write(mobileText);
                                    outputWrite.close();
                                    callIntent(mobileText);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });
    }
    public void callIntent(String data){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("mobile", data);
        startActivity(intent);
        finish();
    }
}