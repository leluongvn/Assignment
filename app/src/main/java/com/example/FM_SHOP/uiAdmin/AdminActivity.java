package com.example.FM_SHOP.uiAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.FM_SHOP.R;
import com.example.FM_SHOP.uiUser.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    BottomNavigationView mNavigationView;
    FrameLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mNavigationView = findViewById(R.id.bottomNavigation);
        mLayout = findViewById(R.id.frameAdmin);

        mNavigationView.setOnNavigationItemSelectedListener(listener);
        mNavigationView.setSelectedItemId(R.id.itemHome);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragmentSelected = null;

            switch (item.getItemId()) {
                case R.id.itemHome: {
                    mFragmentSelected = new HomeFragment();
                    break;
                }
                case R.id.itemConfirm: {
                    mFragmentSelected = new ConfirmFragment();
                    break;
                }
                case R.id.itemAnalysis: {
                    mFragmentSelected = new AccountFragment();
                    break;
                }
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameAdmin, mFragmentSelected)
                    .commit();

            return true;
        }
    };
}