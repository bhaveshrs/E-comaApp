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

public class AdapterForProduct extends RecyclerView.Adapter<AdapterForProduct.ViewHolder> {

    Context context;
    Activity activity;
    ArrayList<GetSetProducts> getSetProductsArrayList;

    public AdapterForProduct(Context context, Activity activity, ArrayList<GetSetProducts> getSetProductsArrayList) {
        this.context = context;
        this.activity = activity;
        this.getSetProductsArrayList = getSetProductsArrayList;
    }

    @NonNull
    @Override
    public AdapterForProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sublayout_for_product_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForProduct.ViewHolder holder, int position) {

        GetSetProducts getSetProducts = getSetProductsArrayList.get(position);

        holder.tvpname.setText(getSetProducts.getPname());
        holder.tvpdetail.setText(getSetProducts.getPdetail());
        holder.tvporip.setText("Rs."+getSetProducts.getPprice());
        holder.tvpmrp.setText("Rs."+getSetProducts.getPmrp());
        holder.tvpdiscount.setText(getSetProducts.getPdiscount()+"%");



        Glide.with(context).load(getSetProducts.getPimg()).error(R.drawable.ic_baseline_add_24).into(holder.ivpimg);

    }

    @Override
    public int getItemCount() {
        return getSetProductsArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView ivpimg;

        TextView tvpname,tvpdetail,tvpmrp,tvporip,tvpdiscount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivpimg = itemView.findViewById(R.id.ivpimg);
            tvpname = itemView.findViewById(R.id.tvpname);
            tvpdetail = itemView.findViewById(R.id.tvpdetail);
            tvpmrp = itemView.findViewById(R.id.tvpmrp);
            tvporip = itemView.findViewById(R.id.tvporip);
            tvpdiscount = itemView.findViewById(R.id.tvpdiscount);
        }
    }
}
