package com.mazad.mazadangy.adapter;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mazad.mazadangy.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class VH {

    public static class slidervH extends SliderViewAdapter.ViewHolder {
        public ImageView imageViewBackground;
        public View itemView;

        public slidervH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imgeId);
            this.itemView = itemView;

        }
    }
}





