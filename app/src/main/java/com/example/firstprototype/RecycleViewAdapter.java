package com.example.firstprototype;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<ProducerModel> producersList;
    Context context;

    // constructor
    public RecycleViewAdapter(List<ProducerModel> producers, Context context) {
        this.producersList = producers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    // set all value in the card template with the correct text and image
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProducerModel currentProducer = producersList.get(position);
        String type = currentProducer.getType();
        if(type.equals("Butcher")){
            holder.iv_typePicture.setImageResource(R.drawable.meat);
        } else if(type.equals("Grocery")){
            holder.iv_typePicture.setImageResource(R.drawable.vegetable);
        } else if(type.equals("Bakery")){
            holder.iv_typePicture.setImageResource(R.drawable.bread);
        } else if(type.equals("none")){
            holder.iv_typePicture.setImageResource(R.drawable.type_none_image);
        } else {
            // must be diary
            holder.iv_typePicture.setImageResource(R.drawable.cheese);
        }
        holder.tv_companyName.setText(currentProducer.getCompanyName());
        holder.tv_fullName.setText(currentProducer.getFirstName() + " " + currentProducer.getSurname());
        holder.tv_description.setText(currentProducer.getDescription());

        // set a click listener and open producer profile (or map if the list is empty)
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentProducer.getType().equals("none")) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyShared", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("companyName", currentProducer.getCompanyName());
                    editor.apply();

                    final Intent i = new Intent(context, ProducerProfile.class);
                    context.startActivity(i);
                } else {
                    final Intent i = new Intent(context, MapsActivity.class);
                    context.startActivity(i);
                }
            }
        });
    }
    // a default function
    @Override
    public int getItemCount() {
        return producersList.size();
    }

    // implementation of view holder class as it is very short
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_typePicture;
        TextView tv_companyName, tv_fullName, tv_description;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_typePicture = itemView.findViewById(R.id.categoryIcon);
            tv_companyName = itemView.findViewById(R.id.companyNameInList);
            tv_fullName = itemView.findViewById(R.id.fullNameInList);
            tv_description = itemView.findViewById(R.id.descriptionInList);
            parentLayout = itemView.findViewById(R.id.oneLineListItem);

        }
    }
}
