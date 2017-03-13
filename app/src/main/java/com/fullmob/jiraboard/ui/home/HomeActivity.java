package com.fullmob.jiraboard.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.home.cameraboard.CaptureBoardFragment;
import com.fullmob.jiraboard.ui.login.LoginActivity;
import com.fullmob.jiraboard.ui.projects.ProjectsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private int currentSelection = 0;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.fragment_container) View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_camera:
                        onCaptureBoardClicked();
                        break;

                    case R.id.bottom_nav_tickets:
                        onBottomNavTicketsClicked();
                        break;


                    case R.id.bottom_nav_settings:
                        break;
                }

                return true;
            }
        });
    }

    private void onCaptureBoardClicked() {
        if (currentSelection != R.id.bottom_nav_camera) {
            CaptureBoardFragment captureBoardFragment = CaptureBoardFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, captureBoardFragment, CaptureBoardFragment.TAG)
                .commit();
        }
    }

    private void onBottomNavTicketsClicked() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_camera:
                startActivity(new Intent(this, QRActivity.class));
                break;

            case R.id.nav_gallery:
                break;

            case R.id.nav_slideshow:
                break;

            case R.id.nav_printing:
                break;

            case R.id.nav_projects:
                startActivity(new Intent(this, ProjectsActivity.class));
                finish();
                break;

            case R.id.nav_logout:
                getApp().getMainComponent().getEncryptedStorage().deletePassword();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private CaptureBoardFragment getBoardFragment() {
        CaptureBoardFragment fragment = (CaptureBoardFragment) getSupportFragmentManager().findFragmentByTag(CaptureBoardFragment.TAG);

        return fragment;
    }
}
