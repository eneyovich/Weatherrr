package com.dzondza.vasya.weatherrr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private DrawerLayout drawerLayout;
    private String dialogCityText;
    private View dialogView;
    private Toolbar toolbar;
    public static final String CITY_DIALOG_KEY = "KEY";
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        createCityDialog().show();


        View navigButtonView = navigationView.getMenu().findItem(R.id.nav_city_button).getActionView();
        Button cityButton = navigButtonView.findViewById(R.id.city_button_id);
        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCityDialog().show();
            }
        });

        fragment = new TodayFragment();

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_weatherToday));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_weatherToday:
                fragment = new TodayFragment();
                break;
            case R.id.nav_weather5days :
                fragment = new FiveDaysFragment();
                break;
            case R.id.nav_weather16days :
                fragment = new SixteenDaysFragment();
                break;
            case R.id.nav_share :
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, "enjoy Weatherrr app");
                Intent.createChooser(intentShare, "Weatherrr");
                startActivity(intentShare);
                break;
        }

        if (fragment != null) {

            //attaches text(city name), written in cityEditText  to fragment
            Bundle args = new Bundle();
            args.putString(CITY_DIALOG_KEY, dialogCityText);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Dialog window to write in city and open TodayFragment with this city's forecast
    private Dialog createCityDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.city_dialog, null, false);
        dialog.setView(dialogView);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText cityEditText = dialogView.findViewById(R.id.city_edit_text_id);
                dialogCityText = cityEditText.getText().toString();
                toolbar.setTitle(dialogCityText.toUpperCase());

                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_weatherToday));

                dialogInterface.dismiss();
            }
        });

        return dialog.create();
    }
}