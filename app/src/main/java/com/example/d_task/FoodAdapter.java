package com.example.d_task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d_task.model.Food;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Food> food_list;
    private Context context;
    private OnRowClickListener listener;

    public FoodAdapter(List<Food> foodList, Context context, OnRowClickListener listener) {
        this.food_list = foodList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnRowClickListener {
        void onItemClick (int position);
        void onClickShare (int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View foodview = LayoutInflater.from(context).inflate(R.layout.food, parent, false);
        return new ViewHolder(foodview, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.descrip_TextView.setText(food_list.get(i).getDescrip());
        viewHolder.title_TextView.setText(food_list.get(i).getTitle());

        // Setting the image
        String path = food_list.get(i).getImage_path();
        if (path != null){
            File image = new File(path);
            Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (image.exists()) {
                viewHolder.food_ImageView.setImageBitmap(myBitmap);
            }
            else
            {
                viewHolder.food_ImageView.setImageResource(R.drawable.no_image);
            }
        }
        else
        {
            viewHolder.food_ImageView.setImageResource(R.drawable.no_image);
        }


    }

    @Override
    public int getItemCount() {
        return food_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView food_ImageView;
        public TextView title_TextView;
        public TextView descrip_TextView;
        public ImageButton share_Button;
        public OnRowClickListener onRowClickListener;


        public ViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);
            food_ImageView = itemView.findViewById(R.id.foodImageView);
            title_TextView = itemView.findViewById(R.id.foodtitleTextView);
            descrip_TextView = itemView.findViewById(R.id.fooddescripTextView);
            share_Button = itemView.findViewById(R.id.shareButton);
            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(v -> onRowClickListener.onItemClick(this.getAdapterPosition()));
            share_Button.setOnClickListener(v -> onRowClickListener.onClickShare(this.getAdapterPosition()));

        }

//        public void onItemClick(View v) {
//            onRowClickListener.onItemClick(getAdapterPosition());
//        }
//
//        public void onClickShare(View v) {
//            onRowClickListener.onItemClick(getAdapterPosition());
//        }
    }

}
