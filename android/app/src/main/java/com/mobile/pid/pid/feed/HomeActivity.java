package com.mobile.pid.pid.feed;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.feed.fragments.BuscarFragment;
import com.mobile.pid.pid.feed.fragments.FeedFragment;
import com.mobile.pid.pid.feed.fragments.PerfilFragment;
import com.mobile.pid.pid.feed.fragments.TurmasFragment;

public class HomeActivity extends AppCompatActivity
{
    private BottomNavigationView home_menu;
    private FrameLayout          home_frame;

    private FeedFragment   feedFragment;
    private BuscarFragment buscarFragment;
    private TurmasFragment turmasFragment;
    private PerfilFragment perfilFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_menu  = (BottomNavigationView) findViewById(R.id.home_menu);
        home_frame = (FrameLayout) findViewById(R.id.home_frame);

        feedFragment   = new FeedFragment();
        buscarFragment = new BuscarFragment();
        turmasFragment = new TurmasFragment();
        perfilFragment = new PerfilFragment();

        BottomNavigationViewHelper.disableShiftMode(home_menu);

        setFragment(feedFragment);

        home_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_feed:
                        setFragment(feedFragment);
                        return true;

                    case R.id.menu_search:
                        setFragment(buscarFragment);
                        return true;
                    case R.id.menu_classes:
                        setFragment(turmasFragment);
                        return true;
                    case R.id.menu_profile:
                        setFragment(perfilFragment);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, fragment);
        fragmentTransaction.commit();
    }
}

