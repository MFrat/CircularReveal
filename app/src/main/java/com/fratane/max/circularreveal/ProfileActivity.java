package com.fratane.max.circularreveal;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment == null){
            fragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
