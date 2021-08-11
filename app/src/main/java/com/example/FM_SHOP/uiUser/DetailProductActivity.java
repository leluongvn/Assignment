package com.example.FM_SHOP.uiUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;

public class DetailProductActivity extends AppCompatActivity {
    Intent mIntentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        mIntentProduct = getIntent();

        Product product = (Product) mIntentProduct.getSerializableExtra("product");

        if (product != null) {
            setData(product);
        }
    }

    private void setData(Product product) {

    }

}