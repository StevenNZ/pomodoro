package com.example.pomodoro;

import android.os.Bundle;

import com.example.pomodoro.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ConstraintLayout fragmentLayout;
    private ConstraintLayout mainMenuInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainMenu, R.id.pomodoro).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        fragmentLayout = findViewById(R.id.fragmentLayout);
        mainMenuInfoLayout = findViewById(R.id.mainMenuInfoLayout);

        mainMenuInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuInfoLayout.setVisibility(View.INVISIBLE);
                fragmentLayout.setAlpha(1.0f);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_info) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            int id = navController.getCurrentDestination().getId();

            fragmentLayout.setAlpha(0.5f);

            if (id == R.id.mainMenu) {
                mainMenuInfoLayout.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}