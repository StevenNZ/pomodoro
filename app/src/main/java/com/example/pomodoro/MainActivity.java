package com.example.pomodoro;

import android.os.Bundle;

import com.example.pomodoro.databinding.ActivityMainBinding;
import com.google.firebase.firestore.auth.User;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private View navHostFrag;
    private ConstraintLayout mainMenuInfoLayout;
    private ConstraintLayout pomodoroInfoLayout;
    private ConstraintLayout shopInfoLayout;
    private ImageView backgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainMenu, R.id.pomodoro).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navHostFrag = findViewById(R.id.nav_host_fragment_content_main);
        mainMenuInfoLayout = findViewById(R.id.mainMenuInfoLayout);
        pomodoroInfoLayout = findViewById(R.id.pomodoroInfoLayout);
        shopInfoLayout = findViewById(R.id.shopInfoLayout);
        backgroundImageView = findViewById(R.id.globalImage);

        mainMenuInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenuInfoLayout.setVisibility(View.INVISIBLE);
                navHostFrag.setAlpha(1.0f);
            }
        });

        pomodoroInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroInfoLayout.setVisibility(View.INVISIBLE);
                navHostFrag.setAlpha(1.0f);
            }
        });

        shopInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopInfoLayout.setVisibility(View.INVISIBLE);
                navHostFrag.setAlpha(1.0f);
            }
        });

        updateBackground();
    }

    protected void updateBackground() {
        backgroundImageView.setImageURI(UserAccount.getUriBackground());
    }

    protected void checkDarkMode(String bgName) {
        if (bgName.equals("Background Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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

            navHostFrag.setAlpha(0.25f);

            // displays the user info for the respective layout
            if (id == R.id.mainMenu) {
                mainMenuInfoLayout.setVisibility(View.VISIBLE);
            } else if (id == R.id.pomodoro) {
                pomodoroInfoLayout.setVisibility(View.VISIBLE);
            } else if (id == R.id.shop) {
                shopInfoLayout.setVisibility(View.VISIBLE);
            } else {
                navHostFrag.setAlpha(1f);
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

    @Override
    public void onBackPressed() {
        navHostFrag.setAlpha(1.0f);
    }
}