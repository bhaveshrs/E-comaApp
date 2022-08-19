package com.at.mytestapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterForCategory extends RecyclerView.Adapter<AdapterForCategory.ViewHolder> {

    ArrayList<GetSetCategory> getSetCategoryArrayList;
    Context context;
    Activity activity;

    public AdapterForCategory(ArrayList<GetSetCategory> getSetCategoryArrayList, Context context, Activity activity) {
        this.getSetCategoryArrayList = getSetCategoryArrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterForCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sublayout_for_category,null);

        return new AdapterForCategory.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForCategory.ViewHolder holder, int position) {

        GetSetCategory getSetCategory = getSetCategoryArrayList.get(position);
        holder.catid = getSetCategory.getCatid();

        holder.tvcatname.setText(getSetCategory.getCatname());

       // Log.e("cs","image =>"+getSetCategory.getCatimage());

        Glide.with(context).load(getSetCategory.getCatimage()).error(R.drawable.ic_baseline_add_24).into(holder.ivcatimg);


    }

    @Override
    public int getItemCount() {
        return getSetCategoryArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvcatname;
        ImageView ivcatimg;

        String catid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvcatname = itemView.findViewById(R.id.tvcatname);
            ivcatimg = itemView.findViewById(R.id.ivcatimg);
        }
    }
}
