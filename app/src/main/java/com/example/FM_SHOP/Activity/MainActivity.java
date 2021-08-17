package com.example.FM_SHOP.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.FM_SHOP.R;
import com.example.FM_SHOP.uiUser.AccountFragment;
import com.example.FM_SHOP.uiUser.CartFragment;
import com.example.FM_SHOP.uiUser.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mNavigationView;
    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
    }

    private void mapping() {

        mNavigationView = findViewById(R.id.bottomNavigationUser);
        mFrameLayout = findViewById(R.id.frameUser);
        mNavigationView.setOnNavigationItemSelectedListener(listener);
        mNavigationView.setSelectedItemId(R.id.itemStore);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragmentSelected = null;

            switch (item.getItemId()) {
                case R.id.itemStore: {
                    mFragmentSelected = new StoreFragment();
                    break;
                }
                case R.id.itemCart: {
                    mFragmentSelected = new CartFragment();
                    break;
                }
                case R.id.itemAnalysis: {
                    mFragmentSelected = new AccountFragment();
                    break;
                }
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameUser, mFragmentSelected)
                    .commit();

            return true;
        }
    };

}