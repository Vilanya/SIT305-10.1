package com.example.d_task.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


import com.example.d_task.model.Food;
import com.example.d_task.model.User;
import com.example.d_task.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

//    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // USER TABLE creation
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_USERS + "(" + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + Util.FULLNAME + " TEXT , " + Util.EMAIL + " TEXT , "+ Util.PHONE + " TEXT, " + Util.ADDRESS + " TEXT , " + Util.PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);
        // FOOD TABLE creation
        String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.TABLE_NAME_FOOD + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Util.USER_ID + " INTEGER," + Util.FOOD_IMAGE + " TEXT, " + Util.FOOD_TITLE + " TEXT, " + Util.FOOD_DESCRIP
            + " TEXT, " + Util.DATE +  " STRING, " + Util.PICKUP_TIMES + " INTEGER, " + Util.QUANTITY + " INTEGER, " + Util.LOCATION + " TEXT)";
        db.execSQL(CREATE_FOOD_TABLE);
//        String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.TABLE_NAME_FOOD + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + Util.USER_ID + " INTEGER , " + Util.FOOD_IMAGE + " TEXT , "+ Util.FOOD_TITLE + " TEXT, " + Util.FOOD_DESCRIP + " TEXT , " + Util.DATE + " TEXT , " + Util.PICKUP_TIMES + " TEXT , " + Util.QUANTITY + " INTEGER , " + Util.LOCATION + " TEXT)";
//       // String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.TABLE_NAME_FOOD + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + Util.USER_ID + " INTEGER , " + Util.FOOD_IMAGE + " TEXT , " + Util.FOOD_TITLE + " TEXT , " + Util.FOOD_DESCRIP + " TEXT , " + Util.DATE +  " TEXT, " + Util.PICKUP_TIMES + " TEXT , " + Util.QUANTITY + " INTEGER , " + Util.LOCATION + " TEXT)";
//        db.execSQL(CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_USER_TABLE = "DROP TABLE IF EXISTS";
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_USERS});
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_FOOD});

        onCreate(db);

    }

    public long insertUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FULLNAME, user.getName());
        contentValues.put(Util.EMAIL, user.getEmail());
        contentValues.put(Util.PHONE, user.getPhone());
        contentValues.put(Util.ADDRESS, user.getAddress());
        contentValues.put(Util.PASSWORD, user.getPassword());
        long newRowId = db.insert(Util.TABLE_NAME_USERS, null, contentValues);
        db.close(); //to prevent memory leaks.
        return newRowId;
    }


//    public User fetchUser(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        User user;
//        //Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME + " WHERE " + Util.NOTE_NAME + "=" + name + "AND " + Util.NOTE_CONTENT + "=" + content, null);
//        Cursor cursor = db.query(Util.TABLE_NAME_USERS, new String[]{Util.USER_ID}, Util.EMAIL + "=? and " + Util.PASSWORD + "=?",
//            new String[] {email, password}, null, null, null);
//
//        user = new User(cursor.getString(cursor.getColumnIndex(Util.FULLNAME)), cursor.getString(cursor.getColumnIndex(Util.EMAIL)),cursor.getString(cursor.getColumnIndex(Util.PHONE)),cursor.getString(cursor.getColumnIndex(Util.ADDRESS)),cursor.getString(cursor.getColumnIndex(Util.PASSWORD)));
//        db.close();
//        return user;
//    }

        public int fetchUser(String email, String password) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(Util.TABLE_NAME_USERS, new String[]{Util.USER_ID}, Util.EMAIL + "=? and " + Util.PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
            if(cursor.moveToFirst()){
                db.close();
                return cursor.getInt(cursor.getColumnIndex(Util.USER_ID));
            }
            else{
                db.close();
                return -1;
            }

        }


        //FOOD Methods
        public long insertFood(Food food){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.USER_ID, food.getUser_id());
            contentValues.put(Util.FOOD_IMAGE, food.getImage_path());
            contentValues.put(Util.FOOD_TITLE, food.getTitle());
            contentValues.put(Util.FOOD_DESCRIP, food.getDescrip());
            contentValues.put(Util.DATE, food.getDate());
            contentValues.put(Util.LOCATION, food.getLocation());
            contentValues.put(Util.PICKUP_TIMES, food.getTime());
            contentValues.put(Util.QUANTITY, food.getQuantity());
            long newRowId = db.insert(Util.TABLE_NAME_FOOD, null, contentValues);
            db.close(); //to prevent memory leaks.
            return newRowId;
        }

    public List<Food> fetchFoodList() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> food_list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME_FOOD, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Food food = new Food();
                food.setUser_id((cursor.getInt(cursor.getColumnIndex(Util.USER_ID))));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.FOOD_TITLE)));
                food.setImage_path(cursor.getString(cursor.getColumnIndex(Util.FOOD_IMAGE)));
                food.setDescrip(cursor.getString(cursor.getColumnIndex(Util.FOOD_DESCRIP)));
                food.setDate(cursor.getString(cursor.getColumnIndex(Util.DATE)));
                food.setTime(cursor.getString(cursor.getColumnIndex(Util.PICKUP_TIMES)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.QUANTITY)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.LOCATION)));
                food_list.add(food);
//                int user_ID = cursor.getInt(cursor.getColumnIndex(Util.USER_ID));
//                String title = cursor.getString(cursor.getColumnIndex(Util.FOOD_TITLE));
//                String descript = cursor.getString(cursor.getColumnIndex(Util.FOOD_DESCRIP));
//                food_list.add(new Food(user_ID,title,descript));
                cursor.moveToNext();
            }
        }
        db.close();
        return food_list;
    }

    public List<Food> fetchFoodList(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> food_list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME_FOOD + " WHERE " + Util.USER_ID + "= ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Food food = new Food();
                food.setUser_id((cursor.getInt(cursor.getColumnIndex(Util.USER_ID))));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.FOOD_TITLE)));
                food.setImage_path(cursor.getString(cursor.getColumnIndex(Util.FOOD_IMAGE)));
                food.setDescrip(cursor.getString(cursor.getColumnIndex(Util.FOOD_DESCRIP)));
                food.setDate(cursor.getString(cursor.getColumnIndex(Util.DATE)));
                food.setTime(cursor.getString(cursor.getColumnIndex(Util.PICKUP_TIMES)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.QUANTITY)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.LOCATION)));
                food_list.add(food);
                cursor.moveToNext();
            }
        }
        db.close();
        return food_list;
    }



}
