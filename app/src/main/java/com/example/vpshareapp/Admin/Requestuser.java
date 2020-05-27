package com.example.vpshareapp.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vpshareapp.Adapter.AdapterAllCommander;
import com.example.vpshareapp.Adapter.AdapterRequest;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.Model.ModelRequest;
import com.example.vpshareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Requestuser extends Fragment {
    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;

    AdapterRequest adapterRequest;
    List<ModelRequest> requestList;

    RecyclerView recyclerView;

    TextView checkLocationBtn;

    String MyID;

    public Requestuser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_requestuser, container, false);

        //init
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        firebaseAuth= FirebaseAuth.getInstance();
        MyID=firebaseAuth.getUid();
        // Toast.makeText(getActivity(), ""+MyID, Toast.LENGTH_SHORT).show();

        requestList=new ArrayList<>();

        //get all bag locations


        getallRequest();
       // checkforuserlogin();


        return view;
    }

    private void getallRequest() {


        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("request");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelRequest modelRequest= ds.getValue(ModelRequest.class);


                    requestList.add(modelRequest);


                    adapterRequest = new AdapterRequest(getActivity(), requestList);

                    recyclerView.setAdapter(adapterRequest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
