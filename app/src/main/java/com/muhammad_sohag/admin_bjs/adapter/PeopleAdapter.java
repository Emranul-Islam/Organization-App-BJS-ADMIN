package com.muhammad_sohag.admin_bjs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muhammad_sohag.admin_bjs.Chada;
import com.muhammad_sohag.admin_bjs.UpdateData;
import com.muhammad_sohag.admin_bjs.model.PeopleModel;
import com.muhammad_sohag.admin_bjs.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private Context context;
    private List<PeopleModel> modelList;

    public PeopleAdapter(Context context, List<PeopleModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.people_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.name.setText(modelList.get(position).getName());
        holder.email.setText(String.format("ইউজার: %s", modelList.get(position).getEmail()));
        holder.password.setText(String.format("পাসওয়ার্ড: %s", modelList.get(position).getPassword()));
        holder.number.setText(String.format("নাম্বার: %s", modelList.get(position).getNumber()));
        //Convert String To Photo
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(modelList.get(position).getUrl())
                .into(holder.photo);



        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sending ID
                Intent editInten = new Intent(context, UpdateData.class);
                editInten.putExtra("uid",modelList.get(position).getUid());
                context.startActivity(editInten);
                Toast.makeText(context, "Edit Option Clicked  " + position, Toast.LENGTH_SHORT).show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sending ID
                Intent addChada = new Intent(context, Chada.class);
                addChada.putExtra("uid",modelList.get(position).getUid());
                addChada.putExtra("name",modelList.get(position).getName());
                context.startActivity(addChada);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView edit;
        private TextView name,email,password, number;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.p_item_image);
            edit = itemView.findViewById(R.id.p_item_edit);
            name = itemView.findViewById(R.id.p_item_name);
            email = itemView.findViewById(R.id.p_item_email);
            password = itemView.findViewById(R.id.p_item_pass);
            number = itemView.findViewById(R.id.p_item_number);

        }
    }
}
