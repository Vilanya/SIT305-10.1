package com.example.d_task;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.d_task.model.Food;

import org.w3c.dom.Text;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Food> food_list;
    private Context context;
    //Mock price per food item
    private static final double PRICE = 12.99;

    public CartAdapter(List<Food> food_list, Context context, CartActivity cartActivity) {
        this.food_list = food_list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Instance variable
        private TextView title;
        private TextView quantity;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cartTitle);
            quantity = itemView.findViewById(R.id.cartQuantity);
            price = itemView.findViewById(R.id.cartPrice);
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.context).inflate(R.layout.cart, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder viewHolder, int position) {
         viewHolder.title.setText(this.food_list.get(position).getTitle());
         viewHolder.quantity.setText("x" + Integer.toString(this.food_list.get(position).getQuantity()));
         viewHolder.price.setText("$" + getTotal(this.food_list.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return this.food_list.size();
    }

    public static String getTotal(int quantity){
        double total = quantity * PRICE;
        double roundDbl = Math.round(total*100.0)/100.0;
        return Double.toString(roundDbl);
    }
}
