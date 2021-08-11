package com.example.FM_SHOP.uiAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends AppCompatActivity {
    TextInputEditText mEditTextName, mEditTextPrice, mEditTextAmount;
    ImageView mImageViewUploadImage, mImageViewProduct;
    AppCompatButton mButtonUploadProduct;
    Spinner spinner;
    Bitmap mBitmap = null;
    TextView mTextView;
    final String URL = "http://192.168.1.3:3000";
    List<String> mListType;
    String path_image = "";
    String imageUpload = null;
    Intent mIntentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mapping();
        checkPermissionApp();

        mIntentProduct = getIntent();
        Product product = new Product();
        product = (Product) mIntentProduct.getSerializableExtra("product");

        if (product != null) {
            mTextView.setText("Update product ");
            mEditTextName.setText(product.getName());
            mEditTextAmount.setText(product.getAmount() + "");
            mEditTextPrice.setText((int) product.getPrice() + "");
            String type = product.getType();
            mImageViewProduct.setVisibility(View.VISIBLE);
            for (int i = 0; i < mListType.size(); i++) {
                if (type.equals(mListType.get(i))) {
                    spinner.setSelection(i);
                }
            }
            imageUpload = product.getImage();
            Glide.with(getApplicationContext()).load(product.getImage()).into(mImageViewProduct);

            String id = product.getId();
            mButtonUploadProduct.setText("UPDATE");
            mButtonUploadProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mEditTextName.getText().toString();
                    String typeUd = spinner.getSelectedItem().toString();
                    String amount = mEditTextAmount.getText().toString();
                    String price = mEditTextPrice.getText().toString();

                    updateProduct(id, name, typeUd, amount, price, imageUpload);
                }
            });
        } else {
            mButtonUploadProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addProduct();

                }
            });
        }
        mImageViewUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    private void checkPermissionApp() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            }, 400);
        }
    }

    private void updateProduct(String id, String name, String typeUd, String amount,
                               String price, String imageUpload) {
        if (name.isEmpty() || typeUd.isEmpty() || amount.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please enter full infomation", Toast.LENGTH_SHORT).show();
            return;
        } else if (imageUpload.isEmpty()) {
            Toast.makeText(this, "Take a image ", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService service = RetrofitClient.getInstance().create(ApiService.class);
        Call<String> call = service.updateProduct(id, name, typeUd, Integer.parseInt(amount), Double.parseDouble(price), imageUpload);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200 && response.body().equals("Success")) {
                    Toast.makeText(AddProductActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });
    }

    private void addProduct() {

        String name = mEditTextName.getText().toString();
        String price = mEditTextPrice.getText().toString();
        String amount = mEditTextAmount.getText().toString();
        String type = spinner.getSelectedItem().toString();
        if (name.isEmpty() || price.isEmpty() || amount.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please enter full", Toast.LENGTH_SHORT).show();
            return;
        } else if (imageUpload == null) {
            Toast.makeText(this, "Select image", Toast.LENGTH_SHORT).show();
            return;
        } else {
            insertProduct(name, Double.parseDouble(price), Integer.parseInt(amount), type, imageUpload);
        }

    }

    private void insertProduct(String name, double price, int amount, String type, String image) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        Call<String> call = service.addProduct(name, type, amount, price, image);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200 && response.body().equals("Success fully")) {
                    Toast.makeText(AddProductActivity.this, "Insert success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddProductActivity.this, "Product name already exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR", "INSERT - PRODUCT" + t);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            upLoadImageServer(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mBitmap = bitmap;
                mImageViewProduct.setVisibility(View.VISIBLE);
                mImageViewProduct.setImageURI(data.getData());
                Toast.makeText(this, "Add image success", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Add image fail", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadImageServer(Uri uri) {

        path_image = getRealPathFromURI(uri);
        File file = new File(path_image);
        String file_path = file.getAbsolutePath();

        String[] nameFile = file_path.split("\\.");

        file_path = nameFile[0] + System.currentTimeMillis() + "." + nameFile[1];
        Log.e("PATH", "" + file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile", file_path, requestBody);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<String> call = apiService.uploadImage(part);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Log.e("DATA", "" + response.body());
                    imageUpload = response.body();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 111);
    }


    private void mapping() {

        mListType = new ArrayList<>();
        mListType.add("Sale");
        mListType.add("New style");
        mListType.add("Normal");
        mEditTextName = findViewById(R.id.edtName);
        mEditTextPrice = findViewById(R.id.edtPrice);
        mEditTextAmount = findViewById(R.id.edtAmount);
        mImageViewProduct = findViewById(R.id.imvProduct);
        mImageViewUploadImage = findViewById(R.id.btnAddImage);
        mButtonUploadProduct = findViewById(R.id.btnUploadProduct);
        mTextView = findViewById(R.id.tvAdd);
        spinner = findViewById(R.id.spnType);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item, mListType);
        spinner.setAdapter(adapter);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}