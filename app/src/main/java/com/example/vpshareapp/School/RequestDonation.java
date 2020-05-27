package com.example.vpshareapp.School;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RequestDonation extends AppCompatActivity {
    //check box
    CheckBox book_cb,pencil_cn,marker_cn,sarnpner_cn;
    EditText other_cn;
    Button send_request;
    String requested =" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_donation);

        book_cb=findViewById(R.id.checkBox_book);
        pencil_cn=findViewById(R.id.checkBox_pencil);
        marker_cn=findViewById(R.id.checkBox_Markers);
        sarnpner_cn=findViewById(R.id.checkBox_Pencil_sharpener);
        other_cn=findViewById(R.id.checkBox_other);
        send_request=findViewById(R.id.send_request);
        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_cb.isChecked()){
                    requested +="Books ";
                }
                if(pencil_cn.isChecked()){
                    requested +="Pencil ";
                }
                if(marker_cn.isChecked()){
                    requested +="Maker ";
                }
                if(sarnpner_cn.isChecked()){
                    requested +="Pencil Sarpner ";
                }
                if(!other_cn.getText().toString().equals("")){
                    requested +=other_cn.getText().toString();
                }
                if(requested.equals("")) {
                    Toast.makeText(RequestDonation.this, "you should select atleast one details ", Toast.LENGTH_SHORT).show();
                }
                else {
                    //send request
                    sendrequest(requested);
                }
            }
        });


    }
    private void sendrequest(final String requested) {
        FirebaseUser user;
        user= FirebaseAuth.getInstance().getCurrentUser();
        //getting school data
        Query query= FirebaseDatabase.getInstance().getReference("School").orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name=""+ds.child("Name").getValue();
                    String address=""+ds.child("Address").getValue();
                    String email=""+ds.child("Email").getValue();
                    //staring requesting

                    DatabaseReference request= FirebaseDatabase.getInstance().getReference("DonationRequest");
                    //making bag section it will store bag loction status commanderId userId
                    final HashMap<Object,String> hashMap2=new HashMap<>();
                    hashMap2.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap2.put("Name",name);
                    //default status 0 = bags allocated to User
                    hashMap2.put("Email",email);
                    hashMap2.put("Address",address);
                    hashMap2.put("RequestedDonationStuff",requested);
                    request.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap2);
                    Toast.makeText(RequestDonation.this, "Requested Admin For Donation", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });



    }
}
