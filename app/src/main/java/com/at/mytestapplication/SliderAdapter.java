package com.at.mytestapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private final List<SliderData> mSliderItems;
    Context context;
    Activity activity;


    public SliderAdapter(Context context, Activity activity, ArrayList<SliderData> sliderDataArrayList) {
        this.context = context;
        this.activity = activity;
        this.mSliderItems = sliderDataArrayList;
    }
    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.imgloader,null);

        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderAdapterViewHolder viewHolder, int position) {

        final SliderData sliderItem = mSliderItems.get(position);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgurl())
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_slideshow)
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder{


        ImageView imageViewBackground;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);

            imageViewBackground = itemView.findViewById(R.id.myimage);
        }
    }
}
