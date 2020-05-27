package com.example.vpshareapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.Admin.UserUnderCommander;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.Model.ModelRequest;
import com.example.vpshareapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRequest extends RecyclerView.Adapter<AdapterRequest.MyHolder> {

    Context context;
    List<ModelRequest> requestList;

    public AdapterRequest(Context context, List<ModelRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public AdapterRequest.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_requestuser,parent,false);
        return new AdapterRequest.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRequest.MyHolder holder, int position) {


        final String hisUID=requestList.get(position).getUserid();

        final String userEmail=requestList.get(position).getUseremail();
        final String userName=requestList.get(position).getUsername();
        String address=requestList.get(position).getUseraddress();
        String isauth=requestList.get(position).getIsBagSended();


        //setdata
        holder.mNameTv.setText(userName);
        // Toast.makeText(context, ""+hisUID, Toast.LENGTH_SHORT).show();
        holder.mEmailTv.setText(userEmail);
        holder.addressT.setText(address);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle when click on user
                //goto posts pages

              //  Intent intent=new Intent(context, UserUnderCommander.class);
               // intent.putExtra("commanderId",hisUID);
              //  context.startActivity(intent);

            }
        });
        //set auth button
        if(isauth.equals("0")){
            //authrity  is not given
            holder.giveAuth.setVisibility(View.VISIBLE);
            holder.authText.setText("Bags Sending..");
        }
        else {
            //authrity is given
            holder.giveAuth.setVisibility(View.GONE);
            holder.authText.setText("Bags Sended");

        }
        //handle if auth button  clicks
        holder.giveAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.builder.setMessage("have you sended bag to user for donation?");

                // Set Alert Title
                holder.builder.setTitle("Alert !");
                // the Dialog Box then it will remain show
                holder.builder.setCancelable(true);
                holder.builder
                        .setPositiveButton(
                                "yes",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        //give authrity to that user
                                        Query query= FirebaseDatabase.getInstance().getReference("request").orderByChild("useremail").equalTo(userEmail);
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot  ds:dataSnapshot.getChildren()){
                                                    ds.getRef().child("isBagSended").setValue("1");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                Toast.makeText(context, "Authority Cancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                holder.builder.create().show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mNameTv, mEmailTv, addressT,authText;
        Button giveAuth;

        AlertDialog.Builder builder;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            builder = new AlertDialog.Builder(context,4);
            addressT = itemView.findViewById(R.id.address);
            mNameTv = itemView.findViewById(R.id.nametv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            authText=itemView.findViewById(R.id.authurity_text);
            giveAuth=itemView.findViewById(R.id.giveAtuh);



        }
    }
}


