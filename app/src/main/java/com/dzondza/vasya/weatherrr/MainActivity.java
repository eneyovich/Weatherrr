package com.dzondza.vasya.weatherrr;

import android.app.Dialog;
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

/**
 * creates drawerLayout, toolbar, Dialog view
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String mDialogCityText;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    public static final String CITY_DIALOG_KEY = "KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNavigationDrawer();

        createCityDialog().show();

        createCityButton();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment mFragment = new TodayFragment();
        switch (item.getItemId()) {
            case R.id.nav_weatherToday:
                mFragment = new TodayFragment();
                break;
            case R.id.nav_weather5days :
                mFragment = new FiveDaysFragment();
                break;
            case R.id.nav_weather16days :
                mFragment = new SixteenDaysFragment();
                break;
            case R.id.nav_share :
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, "enjoy Weatherrr app");
                Intent.createChooser(intentShare, "Weatherrr");
                startActivity(intentShare);
                break;
        }

        if (mFragment != null) {
            //attaches text(city name), written in cityEditText  to mFragment
            Bundle args = new Bundle();
            args.putString(CITY_DIALOG_KEY, mDialogCityText);
            mFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_activity_main, mFragment).commit();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void createNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open_drawer, R.string.close_drawer);
        toggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_weatherToday));
    }


    //Dialog window to write in city and open TodayFragment with this city's forecast
    private Dialog createCityDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View mDialogView = getLayoutInflater().inflate(R.layout.view_city_dialog, null, false);
        dialog.setView(mDialogView);

        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Accept", (dialogInterface, i) -> {
            EditText cityEditText = mDialogView.findViewById(R.id.edit_text_city);
            mDialogCityText = cityEditText.getText().toString();
            mToolbar.setTitle(mDialogCityText.toUpperCase());

            onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_weatherToday));

            dialogInterface.dismiss();
        });

        return dialog.create();
    }


    private void createCityButton() {
        View navigButtonView = mNavigationView.getMenu().findItem(R.id.nav_city_button).getActionView();
        Button cityButton = navigButtonView.findViewById(R.id.button_city);
        cityButton.setOnClickListener(view -> createCityDialog().show());
    }
}