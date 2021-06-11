package com.example.d_task.util;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class Util {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_FOOD = "food";

    public static final String USER_ID = "user_id";
    public static final String FULLNAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASSWORD = "password";


    public static final String FOOD_ID = "food_id";
    public static final String FOOD_IMAGE = "food_image";
    public static final String FOOD_TITLE = "food_title";
    public static final String FOOD_DESCRIP = "food_descrip";
    public static final String DATE = "date";
    public static final String PICKUP_TIMES = "pickup_times";
    public static final String QUANTITY = "quantity";
    public static final String LOCATION = "location";

    // PayPal
    public static final String PAYPAL_KEY = "ARhGnZ2rIV819LbjCA5yfYB2QTxMsxNvy_2wXg6HPLgf5iyqBP1s5gh16fcOiU9JJunZWD-fpW7-HfY5";
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;


//    public static final String PAYPAL_RESPONSE_KEY = "response";
//    public static final String PAYPAL_AMOUNT_KEY = "payment_amount";
}

