package com.example.FM_SHOP.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.User;
import com.example.FM_SHOP.uiAdmin.AdminActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mEditTextEmail, mEditTextPassword;
    AppCompatButton mButtonLogin;
    TextView mTextViewForgotPasswordl, mTextViewRegister;
    final String URL = "http://192.168.1.3:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkLogin()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);
        mButtonLogin = findViewById(R.id.btnLogin);
        mEditTextEmail = findViewById(R.id.edtEmail);
        mEditTextPassword = findViewById(R.id.edtPassword);
        mTextViewForgotPasswordl = findViewById(R.id.tvForgotPassword);
        mTextViewRegister = findViewById(R.id.tvRegister);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString();
                String password = mEditTextPassword.getText().toString();
                Login(email, password);
            }
        });
        mTextViewForgotPasswordl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void Login(String email, String password) {

        if (email.equals("admin@gmail.com") && password.equals("Luong1502")) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        Call<User> userCall = service.USER_CALL
                (email, password);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user = response.body();

                    saveLocal(user);
                    Toast.makeText(LoginActivity.this, "Login success "
                            , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Check the information again", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "" + t);
            }
        });


    }

    private boolean checkLogin() {
        SharedPreferences data = getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean check = data.getBoolean("check",false);
        if (check) {
            return true;
        } else {
            return false;
        }
    }

    private void saveLocal(User user) {
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", user.getName());
        editor.putBoolean("check", true);
        editor.apply();


    }
}