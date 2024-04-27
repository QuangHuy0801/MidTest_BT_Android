package com.example.midtest.Ativity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.midtest.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Fragment fragment ;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setControl();
        setEvent();
    }

    private void setEvent() {
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menuStatistic)
                {
                    fragment = new StatisticFragment();
                    Toast.makeText(MainActivity.this,"Thống kê",Toast.LENGTH_SHORT).show();
                }
                if(menuItem.getItemId()==R.id.menuExercise7)
                {
                    fragment = new Exercise7Fragment();
                    Toast.makeText(MainActivity.this,"Bài 7",Toast.LENGTH_SHORT).show();
                }
                if(menuItem.getItemId()==R.id.menuExercise8)
                {
                    fragment = new Exercise8Fragment();
                    Toast.makeText(MainActivity.this,"Bài 8",Toast.LENGTH_SHORT).show();
                }
                drawer.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).commit();
                return false;
            }
        });

    }

    private void setControl() {
        drawer=findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        fragment = new StatisticFragment();
        drawer.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        if(item.getItemId()==R.id.menuStatistic)
        {
            fragment = new StatisticFragment();
            Toast.makeText(MainActivity.this,"Thống kê",Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.menuExercise7)
        {
            fragment = new Exercise7Fragment();
            Toast.makeText(MainActivity.this,"Bài 7",Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.menuExercise8)
        {
            fragment = new Exercise8Fragment();
            Toast.makeText(MainActivity.this,"Bài 8",Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).commit();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom_navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
