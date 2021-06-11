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

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText t_usernameEditText, t_passwordEditText;
    Button loginButton, sUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        t_usernameEditText = findViewById(R.id.usernameEditText);
        t_passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.buttonLogin);
        sUpButton = findViewById(R.id.buttonSUp);

        db = new DatabaseHelper(this);
    }

    public void onClickLogin(View v) {
        Toast.makeText(LoginActivity.this, "Entered Login OnClick", Toast.LENGTH_SHORT).show();
        String username = t_usernameEditText.getText().toString();
        //User user = db.fetchUser(username, t_passwordEditText.getText().toString());
        int user_id = db.fetchUser(username, t_passwordEditText.getText().toString());
        if(user_id >=0){
            Toast.makeText(LoginActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
            Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("current_user_id", user_id);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("CURRENT_USER_ID", user_id);
            editor.apply();
            startActivity(main_intent);
            Toast.makeText(LoginActivity.this, "Start Main Activity", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(LoginActivity.this, "Login Unsuccessful. Check Username/Email.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSup(View v) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}

