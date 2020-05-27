package com.example.vpshareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vpshareapp.Model.ModelAllocatedUser;
import com.example.vpshareapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterImage extends  RecyclerView.Adapter<AdapterImage.MyHolder> {

    Context context;
    public int[]images;


    public AdapterImage(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_image,parent,false);
        return new AdapterImage.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context)
                .load(images[position])
                .centerCrop()
                .placeholder(R.drawable.ic_bag_logo)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView img;
         public MyHolder(@NonNull View itemView) {
             super(itemView);
             img=itemView.findViewById(R.id.img);
         }
     }
}
