package com.mazad.mazadangy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mazad.mazadangy.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MySliderAdapter extends SliderViewAdapter<VH.slidervH> {
    private Context context;
    private List<String> strings;

    public MySliderAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public VH.slidervH onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_place, null);
        return new VH.slidervH(v);
    }

    @Override
    public void onBindViewHolder(VH.slidervH viewHolder, int position) {
        String s=strings.get(position);
//        Glide.with(viewHolder.itemView)
//                .load(s)
//                .into(viewHolder.imageViewBackground);
        Picasso.get().load(s).into(viewHolder.imageViewBackground);

//        switch (position) {
//
//
//            case 0:
//                Glide.with(viewHolder.itemView)
//                        .load("")
//                        .into(viewHolder.imageViewBackground);
//                break;
//            case 1:
//                Glide.with(viewHolder.itemView)
//                        .load("https://img.youm7.com/ArticleImgs/2017/8/5/1431323-1.PNG")
//                        .into(viewHolder.imageViewBackground);
//                break;
//        }
    }

    @Override
    public int getCount() {
        return strings.size();
    }
}
