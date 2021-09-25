package com.example.FM_SHOP.uiUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.FM_SHOP.Activity.LoginActivity;
import com.example.FM_SHOP.R;

public class AccountFragment extends Fragment {

    AppCompatButton button;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        button = view.findViewById(R.id.btnLogoutAccount);
        textView = view.findViewById(R.id.tvNameAccount);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        textView.setText(preferences.getString("name", null));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                startActivity(intent);
            }
        });
        return view;
    }
}
