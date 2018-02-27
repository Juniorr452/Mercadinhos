package com.mobile.pid.pid.feed;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mobile.pid.pid.R;

public class FeedActivity extends AppCompatActivity {

    private BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        bottom_menu = (BottomNavigationView) findViewById(R.id.bottom_menu);

        BottomNavigationViewHelper.disableShiftMode(bottom_menu);

        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });
    }
}

