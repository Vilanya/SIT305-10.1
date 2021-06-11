package com.example.d_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Output;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d_task.data.DatabaseHelper;
import com.example.d_task.model.Food;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodAdapter.OnRowClickListener{

    DatabaseHelper db;
    List<Food> food_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        food_list = db.fetchFoodList();

        //Testing
//        Toast.makeText(MainActivity.this, food_list.get(0).toString(), Toast.LENGTH_LONG).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerMain);
        FoodAdapter foodItemAdapter = new FoodAdapter(food_list, MainActivity.this.getApplicationContext(), MainActivity.this);
        recyclerView.setAdapter(foodItemAdapter);
//        RecyclerView.LayoutManager foodLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onClickMenu(View v) {

        TextView titletv = findViewById(R.id.titletextView);
        ImageButton menu = findViewById(R.id.menuButton);

        PopupMenu popup = new PopupMenu(MainActivity.this, menu);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                String menu_item = item.getTitle().toString();
                if ("Home".equals(menu_item)) {
                    titletv.setText("Discover free food");
                    db = new DatabaseHelper(MainActivity.this);
                    food_list = db.fetchFoodList();

                    RecyclerView recyclerView = findViewById(R.id.recyclerMain);
                    FoodAdapter foodItemAdapter = new FoodAdapter(food_list, MainActivity.this, MainActivity.this);
                    recyclerView.setAdapter(foodItemAdapter);
                    RecyclerView.LayoutManager foodLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(foodLayoutManager);
                    return true;

                } else if ("Account".equals(menu_item)) {
                    return true;

                } else if ("My List".equals(menu_item)) {
                    titletv.setText("My List");
                    db = new DatabaseHelper(MainActivity.this);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);
                    int user_id = sharedPref.getInt("CURRENT_USER_ID", -1);
                    food_list = db.fetchFoodList(user_id);

                    RecyclerView foodItemRecyclerView = findViewById(R.id.recyclerMain);
                    FoodAdapter foodItemAdapter = new FoodAdapter(food_list, MainActivity.this, MainActivity.this);
                    foodItemRecyclerView.setAdapter(foodItemAdapter);
                    RecyclerView.LayoutManager foodLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    foodItemRecyclerView.setLayoutManager(foodLayoutManager);
                    return true;
                } else if ("Cart".equals(menu_item)) {
                    // TODO Start CartActivity intent here
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return true;
                }
            }
        });
        popup.show();
    }

    @Override
    public void onItemClick(int position) {

        // TODO Start FoodDetailsActivity here

        Intent intent = new Intent(MainActivity.this, FoodDetailsActivity.class);
        intent.putExtra("FOOD", food_list.get(position));
        startActivity(intent);

    }

    public void onClickAdd(View v)
    {
        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onClickShare(int position) {
        Food selected = food_list.get(position);
//        Toast.makeText(MainActivity.this, "Clicked " + selectedFood.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_SUBJECT, "" + selected.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, "This was shared with you: " + "\n" + selected.getTitle());
        intent.setType("text/plain");
        Intent share = Intent.createChooser(intent, null);
        startActivity(share);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==RESULT_OK){
            //Return to home page
            Intent intent_main = new Intent(this, MainActivity.class);
            startActivity(intent_main);
            this.finish();
        }
    }


}
