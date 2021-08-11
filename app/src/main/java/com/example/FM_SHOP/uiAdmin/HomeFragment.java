package com.example.FM_SHOP.uiAdmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FM_SHOP.Adapter.ProductAdapterAdmin;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    FloatingActionButton mButtonAdd;
    private static final String URL_API = "http://192.168.1.3:3000";
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        mButtonAdd = view.findViewById(R.id.floatStore);
        recyclerView = view.findViewById(R.id.rcStore);

        toolbar = view.findViewById(R.id.tbStore);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddProduct();
            }
        });

        callApi();
        return view;
    }

    private void callApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<Product>> listCall = service.productList();

        listCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                List<Product> products = new ArrayList<>();

                if (response.code() == 200) {
                    products = response.body();
                    setAdapterProduct(products);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ERROR", "" + t);
            }
        });


    }

    private void setAdapterProduct(List<Product> products) {

        ProductAdapterAdmin adapter = new ProductAdapterAdmin(products, getActivity());
        GridLayoutManager manager1 = new GridLayoutManager
                (getContext(), 2, GridLayoutManager.VERTICAL, false);
//        manager1.sets

        recyclerView.setLayoutManager(manager1);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProductAdapterAdmin.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Product product = products.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("You want ?");
                builder.setNegativeButton("Delete ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(product.getId());
                        adapter.notifyItemRemoved(position);
                        products.remove(product);
                        recyclerView.setAdapter(adapter);
                    }
                });
                builder.setPositiveButton("Update ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), AddProductActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                    }
                });
                builder.show();
            }

        });


    }

    private void deleteProduct(String id) {

        ApiService service = RetrofitClient.getInstance().create(ApiService.class);
        Call<String> call = service.deleteProduct(id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200 && response.body().equals("Success")) {
                    Toast.makeText(getContext(), "Deleted ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR - DELETE PRODUCT", "" + t);
            }
        });

    }


    private void toAddProduct() {
        Intent intent = new Intent(getContext(), AddProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
