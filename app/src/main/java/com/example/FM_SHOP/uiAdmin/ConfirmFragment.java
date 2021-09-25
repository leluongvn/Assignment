package com.example.FM_SHOP.uiAdmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.FM_SHOP.Adapter.OrderAdapter;
import com.example.FM_SHOP.Api.ApiService;
import com.example.FM_SHOP.Api.RetrofitClient;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.OrderSingle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmFragment extends Fragment {
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        mRecyclerView = view.findViewById(R.id.rcConfirm);

        setAdapter();

        return view;
    }

    private void setAdapter() {

        ApiService service = RetrofitClient.getInstance().create(ApiService.class);


        Call<List<OrderSingle>> call = service.LIST_CART();


        call.enqueue(new Callback<List<OrderSingle>>() {
            @Override
            public void onResponse(Call<List<OrderSingle>> call, Response<List<OrderSingle>> response) {
                if (response.code() == 200) {
                    List<OrderSingle> singles;
                    singles = response.body();
                    setData(singles);
                }
            }

            @Override
            public void onFailure(Call<List<OrderSingle>> call, Throwable t) {

            }
        });


    }

    private void setData(List<OrderSingle> data) {


        OrderAdapter adapter = new OrderAdapter(data, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        adapter.clickItem(new OrderAdapter.clickItemOrder() {
            @Override
            public void clickItem(int position, View view) {
                OrderSingle single = data.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Are you sure ?");

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ApiService service = RetrofitClient.getInstance().create(ApiService.class);

                        Call<String> call = service.CALL_UPDATE(single.getId(), 1);

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.code() == 200 && response.body().equals("Success")) {
                                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                                    adapter.notifyItemChanged(position);
                                    mRecyclerView.setAdapter(adapter);
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }

        });

    }
}
