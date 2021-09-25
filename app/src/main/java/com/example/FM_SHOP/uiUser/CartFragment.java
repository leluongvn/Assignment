package com.example.FM_SHOP.uiUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FM_SHOP.Activity.LoginActivity;
import com.example.FM_SHOP.Adapter.CartAdapter;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.OrderSingle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mRecyclerView = view.findViewById(R.id.rcCart);
        getData();

        return view;
    }

    private void getData() {

        ApiService service = RetrofitClient.getInstance().create(ApiService.class);

        Call<List<OrderSingle>> call = service.LIST_CART();

        call.enqueue(new Callback<List<OrderSingle>>() {
            @Override
            public void onResponse(Call<List<OrderSingle>> call, Response<List<OrderSingle>> response) {
                if (response.code() == 200) {
                    List<OrderSingle> singles = response.body();
                    setList(singles);
                }
            }

            @Override
            public void onFailure(Call<List<OrderSingle>> call, Throwable t) {
                Log.e("ERROR GET LIST CART ", "" + t);
            }
        });

    }

    private void setList(List<OrderSingle> singles) {

        CartAdapter adapter = new CartAdapter(singles, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
    }
}
