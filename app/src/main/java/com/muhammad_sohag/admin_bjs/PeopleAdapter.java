package com.muhammad_sohag.admin_bjs;

import android.annotation.SuppressLint;
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

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private String ID = null;
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
        //Convert String To Photo
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(modelList.get(position).getImage())
                .into(holder.photo);



        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sending ID
                ID = modelList.get(position).getId();
                Intent editInten = new Intent(context, UpdateActivity.class);
                editInten.putExtra("USER_ID",ID);
                context.startActivity(editInten);
                Toast.makeText(context, "Edit Option Clicked  " + position, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView edit;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.p_item_image);
            edit = itemView.findViewById(R.id.p_item_edit);
            name = itemView.findViewById(R.id.p_item_name);
        }
    }
}
