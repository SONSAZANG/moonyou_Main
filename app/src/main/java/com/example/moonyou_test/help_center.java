package com.example.moonyou_test;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class help_center extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.help_center) ;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition() ;
                changeView(pos) ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        }) ;
    }

    private void changeView(int index) {
        FrameLayout FAQ = (FrameLayout) findViewById(R.id.FAQ) ;
        FrameLayout one_to_one = (FrameLayout) findViewById(R.id.one_to_one) ;

        switch (index) {
            case 0 :
                FAQ.setVisibility(View.VISIBLE) ;
                one_to_one.setVisibility(View.GONE) ;
                break ;
            case 1 :
                FAQ.setVisibility(View.GONE) ;
                one_to_one.setVisibility(View.VISIBLE) ;
                break ;
        }
    }
}