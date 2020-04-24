package com.muhammad_sohag.admin_bjs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammad_sohag.admin_bjs.R;
import com.muhammad_sohag.admin_bjs.model.BloodModel;

import java.util.List;

public class BloodAdapter extends RecyclerView.Adapter<BloodAdapter.ViewHolder> {
    Context context;
    List<BloodModel> bloodModelList ;

    public BloodAdapter(Context context, List<BloodModel> bloodModelList) {
        this.context = context;
        this.bloodModelList = bloodModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blood_group_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.nameTV.setText(bloodModelList.get(position).getName());
        holder.numberTV.setText(bloodModelList.get(position).getNumber());
        holder.bloodTV.setText(bloodModelList.get(position).getBlood());
        holder.thikanaTV.setText(bloodModelList.get(position).getThikana());

        holder.editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BloodUpdate.class);
                intent.putExtra("id", bloodModelList.get(position).getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTV,numberTV,bloodTV,thikanaTV;
        private ImageView editIV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.bgi_name);
            numberTV = itemView.findViewById(R.id.bgi_number);
            bloodTV = itemView.findViewById(R.id.bgi_bg);
            editIV = itemView.findViewById(R.id.bgi_edit);
            thikanaTV = itemView.findViewById(R.id.bgi_thikana);
        }
    }
}
