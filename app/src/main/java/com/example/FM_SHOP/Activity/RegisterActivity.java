package com.example.FM_SHOP.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.R;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText mEditTextEmail, mEditTextPassword, mEditTextName, mEditTextPhone, mEditTextPassConfirm;
    AppCompatButton mButtonRegister;
    TextView mTextViewLogin;
    final String URL = "http://192.168.1.3:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEditTextName = findViewById(R.id.edtNameRegiste);
        mEditTextEmail = findViewById(R.id.edtEmailRegister);
        mEditTextPassword = findViewById(R.id.edtPasswordRegister);
        mTextViewLogin = findViewById(R.id.tvLogin);
        mButtonRegister = findViewById(R.id.btnRegister);
        mEditTextPassConfirm = findViewById(R.id.edtPasswordRegisterConfirm);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditTextName.getText().toString();
                String email = mEditTextEmail.getText().toString();
                String password = mEditTextPassword.getText().toString();
                String passConfirm = mEditTextPassConfirm.getText().toString();

                register(name, email, password, passConfirm);
            }
        });
    }

    private void register(String name, String email, String password, String pass2) {

        if (name.isEmpty()) {
            Toast.makeText(this, "Name not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be more than 6 character ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(pass2)) {
            Toast.makeText(this, "Confirm password wrong ", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<String> call = service.registerUser(name, email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success fully")) {
                    Toast.makeText(RegisterActivity.this, "Register done", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (response.body().equals("User already exits")) {
                    Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }
}