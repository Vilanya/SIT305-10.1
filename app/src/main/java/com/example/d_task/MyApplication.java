package com.example.d_task;

import android.app.Application;

import com.example.d_task.model.Food;

import java.util.ArrayList;

public class MyApplication extends Application {

    private ArrayList<Food> food_in_cart = new ArrayList<>();
    public ArrayList<Food> getFood_in_Cart() {
        return food_in_cart;
    }
    public void setFood_in_Cart(ArrayList<Food> food) {
        this.food_in_cart = food;
    }
}



