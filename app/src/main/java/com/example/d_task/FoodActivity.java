package com.example.d_task;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.d_task.data.DatabaseHelper;
import com.example.d_task.model.Food;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodActivity extends AppCompatActivity {

    DatabaseHelper db;
    ImageView foodImage;
    String image;

    EditText titleEditText;
    EditText descriptionEditText;
    EditText pickupEditText;
    EditText locationEditText;
    EditText quantityEditText;
    CalendarView calendar_view;
    String date;

    Button add_image_button;
    Button save_food_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodImage = findViewById(R.id.imageViewCamera);
        titleEditText = findViewById(R.id.titleET);
        descriptionEditText = findViewById(R.id.descripET);
        pickupEditText = findViewById(R.id.pickupET);
        locationEditText = findViewById(R.id.locationET);
        quantityEditText = findViewById(R.id.quantityET);
        calendar_view = (CalendarView) findViewById(R.id.calendarView);

        add_image_button = findViewById(R.id.addImageButton);
        save_food_button = findViewById(R.id.saveFoodButton);

        db = new DatabaseHelper(this);

    }

    public void onClickSaveFood(View v) {

        String title = titleEditText.getText().toString();
        String descrip = descriptionEditText.getText().toString();
        String time = pickupEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String location = locationEditText.getText().toString();
        //String date;

//        long event_date = calendar_view.getDate();
//        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            //show the selected date as a toast
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
//                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
//                date = day + "/" + month + "/" + year;
//                Calendar c = Calendar.getInstance();
//                c.set(year, month, day);
//
//            }
//        });

        long date_long = calendar_view.getDate();
        String date = getDate(date_long,"dd/MM/yyyy");


        if(title.equals("") || descrip.equals("") ||
            time.equals("") || quantity.equals("") || location.equals("")){
            Toast.makeText(FoodActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
        }
        else {
            int quantity_num = Integer.parseInt(quantity);

            Food newItem = new Food();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(FoodActivity.this);
            int user_id = sharedPref.getInt("CURRENT_USER_ID", 0);
            newItem.setUser_id(user_id);
            newItem.setImage_path(image);
            newItem.setTitle(title);
            newItem.setDescrip(descrip);
            newItem.setDate(date);
            newItem.setTime(time);
            newItem.setQuantity(quantity_num);
            newItem.setLocation(location);

            long result = db.insertFood(newItem);
            setResult(RESULT_OK, null);
            finish();
        }
    }

    public void onClickImage(View v) {

        //Adding image
        if (ActivityCompat.checkSelfPermission(FoodActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                FoodActivity.this,
                new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                },
                101);
        } else {
            Toast.makeText(FoodActivity.this, "Permission already granted!", Toast.LENGTH_SHORT).show();
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 2);
        }

    }

    protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = intent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap add_image = BitmapFactory.decodeStream(imageStream);
                image = saveBitmap(this, add_image);

                File test = new File(image);
                Bitmap myBitmap = BitmapFactory.decodeFile(test.getAbsolutePath());
                foodImage.setImageBitmap(myBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(FoodActivity.this, "Select image",Toast.LENGTH_LONG).show();
        }
    }

    private String saveBitmap(Context context, Bitmap image) {
        String saved_image_path = null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(new Date());
        String imageFileName = "JPG_" + timeStamp + ".jpg";
        File storageDir = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/FoodRescueApp");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            saved_image_path = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String savedMessage = "Saved";
            Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show();
        }
        return saved_image_path;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}


