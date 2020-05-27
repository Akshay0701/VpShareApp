package com.example.vpshareapp.User;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.MainScreen;
import com.example.vpshareapp.R;
import com.example.vpshareapp.ui.home.HomeFragment;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    String uid;

    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
    //all acitvity component
    TextView userName,userEmail,userPass,userPhone,barCode,userCity,userCountry,userAddress;

    TextView scanQr,request;
    //logout
    TextView  logoutBtn;


    public Account() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_account, container, false);
        //init firebase
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        scanQr=view.findViewById(R.id.scanQr);
        userName=view.findViewById(R.id.account_name);
        userAddress=view.findViewById(R.id.account_address);
        logoutBtn=view.findViewById(R.id.logout);
        userEmail=view.findViewById(R.id.account_email);
        userPass=view.findViewById(R.id.account_password);
        userPhone=view.findViewById(R.id.account_phone);
        barCode=view.findViewById(R.id.account_barcode);
        userCity=view.findViewById(R.id.account_city);
        userCountry=view.findViewById(R.id.account_country);
        request=view.findViewById(R.id.request);


        //request for bags
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference request=FirebaseDatabase.getInstance().getReference("request");
                //making bag section it will store bag loction status commanderId userId
                final HashMap<Object,String> hashMap2=new HashMap<>();
                hashMap2.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap2.put("username",userName.getText().toString());
                //default status 0 = bags allocated to User
                hashMap2.put("useremail",userEmail.getText().toString());
                hashMap2.put("useraddress",userAddress.getText().toString());
                hashMap2.put("isBagSended","0");
                request.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap2);
            }
        });

        //handle scanqr
        scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),User_QrScan.class));
            }
        });
        //handle logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor;
                editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                firebaseAuth.signOut();
                checkforuserlogin();
            }
        });


        Query query=databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name=""+ds.child("Name").getValue();
                    String address=""+ds.child("Address").getValue();
                    String email=""+ds.child("Email").getValue();
                    String pass=""+ds.child("Password").getValue();
                    String phone=""+ds.child("Phone").getValue();
                    String barcode=""+ds.child("Barcode").getValue();
                    String city=""+ds.child("city").getValue();
                    String country=""+ds.child("country").getValue();


                    if(barcode.equals("")){

                        scanQr.setVisibility(View.VISIBLE);
                    }else {
                        TextView scanQr;
                        scanQr=view.findViewById(R.id.scanQr);
                        scanQr.setVisibility(View.GONE);
                    }

                    //init
                    userName.setText(name);
                    userAddress.setText(address);
                    userEmail.setText(email);
                    userPass.setText(pass);
                    userPhone.setText(phone);
                    barCode.setText(barcode);
                    userCity.setText(city);
                    userCountry.setText(country);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });



        checkforuserlogin();
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String isdashboradtutorial=prefs.getString("UserNote","");
        if(isdashboradtutorial.equals("")) {
            tutorialstart();
        }
    }

    private void tutorialstart() {
        TapTargetView.showFor(getActivity(),              // `this` is an Activity
                TapTarget.forView(getActivity().findViewById(R.id.request), "Registration Success", "Now You Can Request For Bag For Donation.")
                        // All options below are optional
                        .dimColor(R.color.colorgray)
                        .titleTextDimen(R.dimen.circledimen)
                        .descriptionTextColor(R.color.colorgray)
                        .outerCircleColor(R.color.colorPrimary)
                        .targetCircleColor(android.R.color.black)
                        .transparentTarget(true)
                        .cancelable(false)
                        .textColor(R.color.colorAccent)
                        .id(2),              // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                        SharedPreferences.Editor editor;
                        editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString("UserNote", "1");//1 value will note as user as seen intro
                        editor.apply();
                        //  Toast.makeText(
                    }


                });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            uid=user.getUid();


        }
        else{
            startActivity(new Intent(getActivity(), MainScreen.class));
            try {
                Objects.requireNonNull(getActivity()).finish();
            }catch (NullPointerException e){

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            SharedPreferences.Editor editor;
            editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.remove("username");
            editor.remove("password");
            editor.apply();
            firebaseAuth.signOut();
            checkforuserlogin();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}
