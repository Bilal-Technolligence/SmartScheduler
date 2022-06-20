package com.example.smartscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartscheduler.util.BaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class MainActivity extends AppCompatActivity implements PDFUtility.OnDocumentClose {
    private DatabaseReference mDatabase;
    String userId;
    private static final String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    TextView tvStudent,tvTeacher;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String token = new BaseUtil(this).getDeviceToken();
            if (token != null && !token.equals(""))
                mDatabase.child("DeviceTokens").child("Teachers").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token")
                        .setValue(token);
        } else {
            startActivity(new Intent(this, Login.class));
        }

        verifystoragepermissions(this);

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

    // verifying if storage permission is given or not
    public static void verifystoragepermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*if (item.getItemId() == R.id.generatePDF) {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            LetsCreatePDF(rootView);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void LetsCreatePDF(View view) {

        // String path = getFilesDir().getAbsolutePath()
        //        + File.separator +"CashCalculatorCounter" + File.separator +Calendar.getInstance().getTimeInMillis()+"_CashHistory.pdf";
       /* String path = Environment.getExternalStorageDirectory().getPath() +
                "/" + "CashHistory.pdf";*/
        String path = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + "CashHistory.pdf";
        } else {
            path = Environment.getExternalStorageDirectory() + "/" + "CashHistory.pdf";
        }
        file = new File(path);

        /*try {
            PDFUtilityCash.createPdf(getApplicationContext(), MainActivity.this, new CashHistory(getSampleData(), etPayeeName.getText().toString(), getIntent().getStringExtra("date"), getIntent().getStringExtra("day"), numToWords((int) tAmount) + " Only"), path, true);
            showPdfSuccessDialog();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Error Creating Pdf", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void showPdfSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        /*View view = layoutInflater.inflate(R.layout.pdf_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button sharepdfbtn = view.findViewById(R.id.btnShare);
        ImageView closebtn = view.findViewById(R.id.close);
        AlertDialog alertDialog = builder.create();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        sharepdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);


                //  Uri imageUri = Uri.parse(MainActivity.imageurl.getAbsolutePath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //Target whatsapp:
                // shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(Intent.createChooser(shareIntent, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "App Not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button viewbtn = view.findViewById(R.id.btnView);
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Uri uri = Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                //  Uri imageUri = Uri.parse(MainActivity.imageurl.getAbsolutePath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_VIEW);
                //Target whatsapp:
                // shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(uri, "application/pdf");

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "No Pdf Viewer install", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialog.show();
*/
    }

    @Override
    public void onPDFDocumentClose(File file) {
    }
}