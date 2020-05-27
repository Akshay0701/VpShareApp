package com.example.vpshareapp.commanderacivty;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CommanderQrScan  extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView scannerView;
    private TextView textView;
    String result="";
    Button allocate_bags;
    String ToDo;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Commanders");
   String previesAllocated="";
    String Allocate="";
    String child = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander_qr_scan);

        Intent intent=getIntent();
        ToDo=intent.getStringExtra("todo");

        //init
        scannerView=findViewById(R.id.zxscan);
        textView=findViewById(R.id.txt_result);

        final Query query=ref.orderByChild("Email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 Allocate="";
                 child = "";
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    child=ds.getKey();
                    Allocate=ds.child("Allocated_Bags").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //request permission
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView.setResultHandler(CommanderQrScan.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(CommanderQrScan.this, "acces deined", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        scannerView.stopCamera();
        super.onStop();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!ToDo.equals("")) {
            if (ToDo.equals("scanBags")) {
                result = result + " " + rawResult.getText();
                textView.setText(result);
                //start acitivty
                SharedPreferences sharedPreferences;
                SharedPreferences.Editor editor;
                sharedPreferences = getSharedPreferences("Remember", MODE_PRIVATE);
                editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String alocated_Bags = prefs.getString("bags", "");
                editor.putString("bags", (alocated_Bags + result));
                editor.apply();
                Intent intent = new Intent(CommanderQrScan.this, CommanderLogin.class);
                startActivity(intent);
            } else if (ToDo.equals("addBagsToCommander")) {
                Log.e("into","run");
                result = result + " " + rawResult.getText();
                textView.setText(result);
                updateAllocate(result);
            }
        }
    }

    private void updateAllocate(final String result) {
        final Query query=ref.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("sdasa","run");
                dataSnapshot.getRef().child(child).child("Allocated_Bags").setValue(Allocate+result);
                Toast.makeText(CommanderQrScan.this, Allocate, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CommanderQrScan.this, CommanderDashBorad.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
