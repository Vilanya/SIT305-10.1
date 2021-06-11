package com.example.d_task;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d_task.data.DatabaseHelper;
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
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    DatabaseHelper db;

    //Paypal
    private static PayPalConfiguration config;
    private static final double PRICE = 12.99;

    private CartAdapter cartAdapter;
    private List<Food> food_list;
    private Button pay_button;
    int quantity = 0;
    String price;

    PayPalPayment thingsToBuy;

    public CartActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new DatabaseHelper(this);
        pay_button = (Button)findViewById(R.id.payButton);
        food_list = ((MyApplication) getApplication()).getFood_in_Cart();
        TextView amountTV = findViewById(R.id.amount_tv);

        RecyclerView cartRecyclerView = (RecyclerView)findViewById(R.id.cartListView);
        CartAdapter cartItemAdapter = new CartAdapter(food_list, CartActivity.this.getApplicationContext(), CartActivity.this);
        cartRecyclerView.setAdapter(cartItemAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < food_list.size(); i++) {
            quantity += food_list.get(i).getQuantity();
        }
        price = CartAdapter.getTotal(quantity);
        amountTV.setText("$" + price);

        //Paypal
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePayment();
            }
        });

        ConfigPaypal();

    }

//    public String getTotal(int quantity){
//        double total = quantity * PRICE;
//        return Double.toString(total);
//    }

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
