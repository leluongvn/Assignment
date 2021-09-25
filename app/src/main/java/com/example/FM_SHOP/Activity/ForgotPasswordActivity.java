package com.example.FM_SHOP.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText mEditTextEmail, mEditTextCode, mEditTextPassword, mEditTextConfirmPassword;
    AppCompatButton mButtonGet;
    TextInputLayout mLayoutPassword, mLayoutConfirmPassword, mInputLayoutEmail, mInputLayoutCode;
    TextView mTextViewVetify;
    final String URL = "http://192.168.1.3:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mapping();

        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString();
                requestForgotPassword(email);
            }
        });

    }

    private void mapping() {
        mButtonGet = findViewById(R.id.btnCode);
        mEditTextEmail = findViewById(R.id.edtEmailVetify);
        mLayoutPassword = findViewById(R.id.tipPassword);
        mLayoutConfirmPassword = findViewById(R.id.tipPasswordConfirm);
        mEditTextCode = findViewById(R.id.edtCode);
        mTextViewVetify = findViewById(R.id.tvVetify);
        mInputLayoutEmail = findViewById(R.id.tipEmail);
        mInputLayoutCode = findViewById(R.id.tipCode);
        mEditTextPassword = findViewById(R.id.edtPassordForgot);
        mEditTextConfirmPassword = findViewById(R.id.edtPasswordConfirm);
    }

    private void requestForgotPassword(String email) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<String> call = service.forgot(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("success")) {
                    mButtonGet.setText("Reset password");
                    mTextViewVetify.setText("");
                    mInputLayoutCode.setVisibility(View.VISIBLE);
                    mEditTextEmail.setEnabled(false);
                    mLayoutPassword.setVisibility(View.VISIBLE);
                    mLayoutConfirmPassword.setVisibility(View.VISIBLE);
//                    mInputLayoutEmail.setVisibility(View.GONE);

                    mButtonGet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //
                            String code = mEditTextCode.getText().toString();
                            String password = mEditTextPassword.getText().toString();
                            String passwordConfirm = mEditTextConfirmPassword.getText().toString();
                            request(code, password, passwordConfirm, mTextViewVetify);
                        }
                    });


                } else {
                    mTextViewVetify.setText("Email not found");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mTextViewVetify.setText("Email not found");
            }
        });


    }

    private void request(String code, String password, String confirmPassword, TextView mTextViewVetify) {

        if (!password.equals(confirmPassword)) {
            mTextViewVetify.setText("Confirm password invalid");
            mTextViewVetify.setTextColor(Color.RED);
            return;
        }
        mTextViewVetify.setText("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<String> call = service.reset(code, confirmPassword);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success update")) {
                    Toast.makeText(ForgotPasswordActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (response.body().equals("Code invalid")) {
                    mTextViewVetify.setText("Code invalid");
                    mTextViewVetify.setTextColor(Color.RED);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Server id down ",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR-SERVER",""+t);
            }
        });


    }
}