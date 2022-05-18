package com.example.smartscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity {
    ArrayList<ScheduleModel> list;
    AdapterAllSchedules adapter;
    RecyclerView recyclerView;
    View NoRecordFoundView;
    DatabaseReference myRef;
    ProgressBar loadingBar;
    ValueEventListener allValueListener=null;
    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();

    Spinner SemesterSpinner;
    Spinner DepartmentSpinner;

    Button SearchButton;

    ArrayAdapter<String> adapterSemester,adapterDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);
        loadingBar = findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);

        myRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.rec);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new AdapterAllSchedules(list, this);

        recyclerView.setAdapter(adapter);

        DepartmentList.add("IT");
        DepartmentList.add("BBA");
        DepartmentList.add("BSM");
        DepartmentList.add("LAW");
        DepartmentList.add("BBA");

        SemesterList.add("1");
        SemesterList.add("2");
        SemesterList.add("3");
        SemesterList.add("4");
        SemesterList.add("5");
        SemesterList.add("6");
        SemesterList.add("7");
        SemesterList.add("8");

        SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);
        SearchButton = findViewById(R.id.search_Schedule_Button);

        adapterSemester = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item, SemesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adapterSemester);

        adapterDepartment = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item, DepartmentList);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapterDepartment);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    @Override
    public void onStop() {
        if (allValueListener != null) {
            myRef.removeEventListener(allValueListener);

        }
        super.onStop();
    }

    private void getData() {
        loadingBar.setVisibility(View.VISIBLE);
        myRef.child("Schedule").addValueEventListener(allValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID= "";
                        String DAY= "";
                        String S_TIME= "";
                        String E_TIME= "";
                        String COURSE= "";
                        String FACULTY= "";
                        String ROOM= "";
                        String SEMESTER= "";
                        String CREDIT_HOUR= "";
                        String DEPARTMENT = "";

                        ID = dataSnapshot.getKey();
                        if (dataSnapshot.hasChild("DAY"))
                            DAY = Objects.requireNonNull(dataSnapshot.child("DAY").getValue()).toString();
                        if (dataSnapshot.hasChild("S_TIME"))
                            S_TIME = dataSnapshot.child("S_TIME").getValue().toString();
                        if (dataSnapshot.hasChild("E_TIME"))
                            E_TIME = dataSnapshot.child("E_TIME").getValue().toString();
                        if (dataSnapshot.hasChild("COURSE"))
                            COURSE = dataSnapshot.child("COURSE").getValue().toString();
                        if (dataSnapshot.hasChild("FACULTY"))
                            FACULTY = dataSnapshot.child("FACULTY").getValue().toString();
                        if (dataSnapshot.hasChild("ROOM"))
                            ROOM = dataSnapshot.child("ROOM").getValue().toString();
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = dataSnapshot.child("SEMESTER").getValue().toString();
                        if (dataSnapshot.hasChild("CREDIT_HOUR"))
                            CREDIT_HOUR = dataSnapshot.child("CREDIT_HOUR").getValue().toString();
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = dataSnapshot.child("DEPARTMENT").getValue().toString();

                        String query1 = DepartmentSpinner.getSelectedItem().toString();
                        String query2 = SemesterSpinner.getSelectedItem().toString() ;

                        if (query1.equals(DEPARTMENT) && query2.equals(SEMESTER))
                            list.add(new ScheduleModel(ID,DAY,S_TIME,E_TIME,COURSE,FACULTY,ROOM,SEMESTER,
                                CREDIT_HOUR,DEPARTMENT));
                    }
                    if (list.isEmpty()) {
                        if (loadingBar.getVisibility() == View.VISIBLE)
                            loadingBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        NoRecordFoundView.setVisibility(View.VISIBLE);
                    } else {
                        NoRecordFoundView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Collections.reverse(list);
                        loadingBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    NoRecordFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.setVisibility(View.GONE);
            }
        });
    }
}