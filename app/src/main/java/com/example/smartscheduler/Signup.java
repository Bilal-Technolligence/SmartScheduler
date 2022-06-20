package com.example.smartscheduler;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smartscheduler.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Signup extends AppCompatActivity {
    Spinner CreditSpinner;
    ArrayAdapter<String> adapterCreditHours;
    ArrayList<String> CreditList = new ArrayList<>();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private EditText editText_FullName, editText_Email, editText_password, editText_confirm_password;
    private FirebaseAuth auth;
    private Uri filePath;
    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        //Input
        editText_FullName = findViewById(R.id.full_name_input);
        editText_Email = findViewById(R.id.email_input);
        editText_password = findViewById(R.id.password_input);
        editText_confirm_password = findViewById(R.id.re_enter_password);
        TextView alreadyRegistered_TextView = findViewById(R.id.newUserTextView);

        ImageView img_Sign_Up = findViewById(R.id.sign_up_imgView);

        CreditSpinner = findViewById(R.id.creditHourSpinner);
        CreditList.add("Teacher");
        CreditList.add("Student");
        adapterCreditHours = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CreditList);
        adapterCreditHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreditSpinner.setAdapter(adapterCreditHours);

        img_Sign_Up.setOnClickListener(v -> {


            if (editText_FullName.getText().toString().trim().equals("")) {
                editText_FullName.setError("Full Name is required");
                editText_FullName.requestFocus();
                return;
            }

            if (editText_Email.getText().toString().trim().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_Email.getText().toString().trim()).matches()) {
                editText_Email.setError("Email is required");
                editText_Email.requestFocus();
                return;
            }

            if (editText_password.getText().toString().equals("")) {
                editText_password.setError("Password is required");
                editText_password.requestFocus();
                return;
            }

            if (editText_password.getText().toString().length() < 8) {
                editText_password.setError("Password length should be greater or equal to 8 characters");
                editText_password.requestFocus();
                return;
            }

            if (!editText_password.getText().toString().equals(editText_confirm_password.getText().toString())) {
                editText_password.setError("Password did not match with confirm password");
                editText_password.requestFocus();
                return;
            }

            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Creating Admin ...");
            pDialog.setCancelable(false);
            pDialog.show();
            String userType = CreditSpinner.getSelectedItem().toString();

            //create user
            auth.createUserWithEmailAndPassword(editText_Email.getText().toString().trim(), editText_password.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            pDialog.setTitleText("Oops...");
                            pDialog.setContentText("Authentication failed , " + task.getException());
                            pDialog.setCancelable(true);
                            pDialog.show();
                        } else {
                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                UserData user = new UserData(editText_FullName.getText().toString(), editText_Email.getText().toString(),userType,uid);
                                uploadDB(user);


                        }
                    });
        });
        alreadyRegistered_TextView.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            this.finish();
        });

    }

    private void uploadDB(UserData user) {
        pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Adding Admin Information to Database ...");
        pDialog.setCancelable(false);
        pDialog.show();

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("AppUsers");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            databaseReference.child(firebaseUser.getUid()).setValue(user);
        }

        pDialog.dismissWithAnimation();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}