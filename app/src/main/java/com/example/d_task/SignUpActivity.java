package com.example.d_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.d_task.data.DatabaseHelper;
import com.example.d_task.model.User;

public class SignUpActivity extends AppCompatActivity {

    EditText fullname, email, phone, address, password, confirm_pw;
    Button button_sign_up;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        confirm_pw = findViewById(R.id.confirmPW);
        button_sign_up = findViewById(R.id.button);

        db = new DatabaseHelper(this);

    }

    public void onClickSignUp(View v) {
        String t_fullname = fullname.getText().toString();
        String t_email = email.getText().toString();
        String t_phone = phone.getText().toString();
        String t_address = address.getText().toString();
        String t_password = password.getText().toString();
        String t_confirm_pw = confirm_pw.getText().toString();

        if(t_password.equals(t_confirm_pw)){
            long result = db.insertUser(new User(t_fullname, t_email, t_phone, t_address, t_password));
            if(result > 0){
                Toast.makeText(SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
    }
}

