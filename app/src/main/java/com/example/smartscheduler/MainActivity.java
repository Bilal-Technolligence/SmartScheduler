package com.example.smartscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String userId;

    TextView tvStudent,tvTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStudent = findViewById(R.id.student);
        tvTeacher = findViewById(R.id.teacher);

        tvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,StudentActivity.class));
            }
        });

        tvTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TeacherActivity.class));
            }
        });

        /*userId = "abc";
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).child("username").setValue("Muhammad Bilal");*/
    }
}