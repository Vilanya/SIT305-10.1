package com.example.d_task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d_task.model.Food;
import com.example.d_task.util.Util;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FoodDetailsActivity extends AppCompatActivity {

    //Paypal
    private static PayPalConfiguration config;
    private static final double PRICE = 12.99;

    PayPalPayment thingsToBuy;
    Button pay;

    private Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        food = (Food)getIntent().getSerializableExtra("FOOD");

        ImageView image = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.textViewTitle);
        TextView descrip = findViewById(R.id.descrip2);
        TextView date = findViewById(R.id.date2);
        TextView time = findViewById(R.id.time2);
        TextView quantity = findViewById(R.id.quantity2);
        TextView location = findViewById(R.id.location2);

        pay = (Button)findViewById(R.id.payNowButton);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePayment();
            }
        });

        ConfigPaypal();

        //setting the image view
        String path = food.getImage_path();
        if (path != null){
            File bmp = new File(path);
            Bitmap myBitmap = BitmapFactory.decodeFile(bmp.getAbsolutePath());
            if (bmp.exists()) {
                image.setImageBitmap(myBitmap);
            }
            else
            {
                image.setImageResource(R.drawable.no_image);
            }
        }
        else
        {
            image.setImageResource(R.drawable.no_image);
        }

        //setting other text views
        title.setText(food.getTitle());
        descrip.setText(food.getDescrip());
        date.setText(food.getDate());
        time.setText(food.getTime());
        quantity.setText(String.valueOf(food.getQuantity()));
        location.setText(food.getLocation());
    }

    public void onClickAddToCart(View v){

        ArrayList<Food> to_cart = ((MyApplication) getApplication()).getFood_in_Cart();
        to_cart.add(food);
        ((MyApplication) getApplication()).setFood_in_Cart(to_cart);
        Toast.makeText(FoodDetailsActivity.this, "Added Item to Cart: " + food.getTitle(), Toast.LENGTH_SHORT).show();

        //Going back to Home
        Intent intent = new Intent(FoodDetailsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void ConfigPaypal() {
        config = new PayPalConfiguration()
            .environment(Util.CONFIG_ENVIRONMENT)
            .clientId(Util.PAYPAL_KEY)
            .merchantName("Paypal Login")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    }

    private void MakePayment() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        thingsToBuy = new PayPalPayment(new BigDecimal(String.valueOf(PRICE)),"AUD","Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent payment = new Intent(this, PaymentActivity.class);
        payment.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
        payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startActivityForResult(payment, Util.REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Util.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment has been cancelled", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == Util.REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization authorization = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (authorization != null){
                    try{
                        Log.i("FuturePaymentExample", authorization.toJSONObject().toString(4));
                        String authorization_code = authorization.getAuthorizationCode();
                        Log.d("FuturePaymentExample", authorization_code);

                        Log.e("paypal", "Future payment code received from paypal: " + authorization_code);

                    }catch (Exception e){
                        Toast.makeText(this, "Failure Occurred", Toast.LENGTH_LONG).show();
                        Log.e("FuturePaymentExample", "Unlikely failure occured: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment has been cancelled", Toast.LENGTH_LONG).show();
                Log.d("FuturePaymentExample", "User has cancelled");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
                Log.d("FuturePaymentExample", "Previous start attempt may have had an invalid config code. Please check docs.");
            }

        }

    }
}
