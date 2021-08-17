package com.example.FM_SHOP.uiUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.FM_SHOP.Activity.MainActivity;
import com.example.FM_SHOP.Adapter.ProductAdapterSmall;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    Intent mIntentProduct;
    RecyclerView mRecyclerView;

    ImageView mImageViewProduct;
    TextView mTextViewName;
    TextView mTextViewPrice;
    Toolbar mToolbar;
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        mRecyclerView = findViewById(R.id.rcPRDDetail);
        mImageViewProduct = findViewById(R.id.imvPRDDetail);
        mTextViewName = findViewById(R.id.tvNamePTD);
        mTextViewPrice = findViewById(R.id.tvPricePRD);
        mToolbar = findViewById(R.id.tbDetailProduct);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mImageView = mToolbar.findViewById(R.id.imvBack);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();
            }
        });
        mIntentProduct = getIntent();
        Product product = (Product) mIntentProduct.getSerializableExtra("product");
        if (product != null) {
            setData(product);
        }
        setListProduct();
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
                Glide.with(DetailProductActivity.this).load(productSelected.getImage()).into(mImageViewProduct);
                mTextViewName.setText(productSelected.getName());
                mTextViewPrice.setText((int) productSelected.getPrice() + " đ");
            }
        });


    }

}