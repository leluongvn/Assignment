package com.example.FM_SHOP.uiUser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.FM_SHOP.Activity.MainActivity;
import com.example.FM_SHOP.Activity.MessageActivity;
import com.example.FM_SHOP.Adapter.ProductAdapterSmall;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.OrderSingle;
import com.example.FM_SHOP.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {
    Intent mIntentProduct;
    RecyclerView mRecyclerView;
    ImageView mImageViewProduct;
    TextView mTextViewName;
    TextView mTextViewPrice;
    Toolbar mToolbar;
    ImageView mImageView;
    private Product mProductCurrent;
    AppCompatButton mButtonToMessage, mButtonAddToCart, mButtonBuyNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        mapping();

        mImageView.setOnClickListener(this);
        mButtonBuyNow.setOnClickListener(this);
        mButtonAddToCart.setOnClickListener(this);
        mButtonToMessage.setOnClickListener(this);

        mIntentProduct = getIntent();
        Product product = (Product) mIntentProduct.getSerializableExtra("product");
        if (product != null) {
            setData(product);
        }
        setListProduct();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack: {
                backHome();
                break;
            }
            case R.id.btnToMessage: {
                toMessage();
                break;
            }
            case R.id.btnBuyNow: {
                toCart();
                break;
            }
        }

    }


    private void toMessage() {
        Intent intent = new Intent(DetailProductActivity.this, MessageActivity.class);
        startActivity(intent);
    }

    private void toCart() {
        ImageView mImageViewIncrease, mImageViewReduced;
        EditText mEditTextAmount;
        AppCompatButton mButtonContinue;
        BottomSheetDialog dialog = new BottomSheetDialog(DetailProductActivity.this);
        dialog.setContentView(R.layout.bottom_sheet_amount);
        mImageViewIncrease = dialog.findViewById(R.id.imvIncrease);
        mImageViewReduced = dialog.findViewById(R.id.imvReduced);
        mEditTextAmount = dialog.findViewById(R.id.edtAmountOrder);
        mButtonContinue = dialog.findViewById(R.id.btnContinue);
        mImageViewIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inCrease(mEditTextAmount);
            }
        });
        mImageViewReduced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduced(mEditTextAmount);
            }
        });
        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(mEditTextAmount.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addProduct(String amount) {


        SharedPreferences data = getSharedPreferences("login", Context.MODE_PRIVATE);
        String nameUser = data.getString("name", null);
        ApiService service = RetrofitClient.getInstance().create(ApiService.class);
        Call<String> call = service.insertCart(mProductCurrent.getName(),
                mProductCurrent.getImage(), amount,
                mProductCurrent.getPrice() + "", nameUser, 0);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success fully")) {
                    Toast.makeText(DetailProductActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });


    }

    private void reduced(EditText mEditTextAmount) {

        int temp = Integer.parseInt(mEditTextAmount.getText().toString());
        temp -= 1;
        mEditTextAmount.setText(temp + "");
        Log.e("DATA", "" + temp);
    }

    private void inCrease(EditText mEditTextAmount) {
        int temp = Integer.parseInt(mEditTextAmount.getText().toString());
        temp += 1;
        mEditTextAmount.setText(temp + "");
        Log.e("DATA", "" + temp);
    }

    private void backHome() {
        Intent intent = new Intent(DetailProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setData(Product product) {
        Glide.with(getApplicationContext()).load(product.getImage()).into(mImageViewProduct);
        int price = (int) product.getPrice();
        mTextViewPrice.setText(price + " đ");
        mTextViewName.setText(product.getName());
        mProductCurrent = product;
        Log.e("ID ", "" + product.getId());
    }

    private void setListProduct() {
        ApiService service = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Product>> listCall = service.productList();

        listCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products;
                products = response.body();
                setAdapter(products);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });
    }

    public void setAdapter(List<Product> productList) {
        ProductAdapterSmall adapterUser = new ProductAdapterSmall(productList, DetailProductActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapterUser);
        mRecyclerView.setLayoutManager(manager);
        adapterUser.setOnclickItem(new ProductAdapterSmall.OnclickItem() {
            @Override
            public void clickItem(int position, View view) {
                Product productSelected = productList.get(position);
                mProductCurrent = productSelected;
                Log.e("ID", "" + productSelected.getId());
                Glide.with(DetailProductActivity.this).load(productSelected.getImage()).into(mImageViewProduct);
                mTextViewName.setText(productSelected.getName());
                mTextViewPrice.setText((int) productSelected.getPrice() + " đ");
            }
        });


    }

    private void mapping() {
        mRecyclerView = findViewById(R.id.rcPRDDetail);
        mImageViewProduct = findViewById(R.id.imvPRDDetail);
        mTextViewName = findViewById(R.id.tvNamePTD);
        mTextViewPrice = findViewById(R.id.tvPricePRD);
        mToolbar = findViewById(R.id.tbDetailProduct);
        mButtonAddToCart = findViewById(R.id.btnToCart);
        mButtonToMessage = findViewById(R.id.btnToMessage);
        mButtonBuyNow = findViewById(R.id.btnBuyNow);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mImageView = mToolbar.findViewById(R.id.imvBack);
    }


}