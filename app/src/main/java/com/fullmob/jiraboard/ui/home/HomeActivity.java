package com.fullmob.jiraboard.ui.home;

import android.app.SearchManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.User;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.BaseFragment;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardFragment;
import com.fullmob.jiraboard.ui.home.statuses.StatusesFragment;
import com.fullmob.jiraboard.ui.home.tickets.SearchFragment;
import com.fullmob.jiraboard.ui.login.LoginActivity;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.projects.ProjectsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements
    NavigationView.OnNavigationItemSelectedListener,
    CaptureBoardFragment.OnFragmentInteractionListener,
    SearchFragment.OnFragmentInteractionListener,
    StatusesFragment.OnIssueStatusesInteractor,
    HomeScreenView
{
    private int currentSelection = 0;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.fragment_container) View fragmentContainer;
    CircleImageView userProfileImage;
    TextView username;
    TextView email;

    @Inject HomeScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getApp().getHomeScreenComponent(this).inject(this);
        ButterKnife.bind(this);
        setupUI();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            onBottomNavTicketsClicked();
            String query = intent.getStringExtra(SearchManager.QUERY);
            getTicketsFragment().onSearchRequested(query);
        } else {
            onCaptureBoardClicked();
        }
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

        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        userProfileImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_image);

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


                    case R.id.bottom_nav_transitions:
                        onStatusesClicked();
                        break;
                }
                currentSelection = item.getItemId();

                return true;
            }
        });
    }

    private void onStatusesClicked() {
        if (currentSelection != R.id.bottom_nav_transitions) {
            setCurrentFragment(StatusesFragment.newInstance(), StatusesFragment.TAG);
        }
    }

    private void onCaptureBoardClicked() {
        if (currentSelection != R.id.bottom_nav_camera) {
            setCurrentFragment(CaptureBoardFragment.newInstance(), CaptureBoardFragment.TAG);
        }
    }

    private void onBottomNavTicketsClicked() {
        if (currentSelection != R.id.bottom_nav_tickets) {
            setCurrentFragment(SearchFragment.newInstance(), SearchFragment.TAG);
        }
    }

    private void setCurrentFragment(BaseFragment captureBoardFragment, String tag) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, captureBoardFragment, tag)
            .commit();
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
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
//            case R.id.nav_camera:
//                startActivity(new Intent(this, QRActivity.class));
//                break;
//
//            case R.id.nav_gallery:
//                break;
//
//            case R.id.nav_slideshow:
//                break;
//
//            case R.id.nav_printing:
//                break;

            case R.id.nav_projects:
                startActivity(new Intent(this, ProjectsActivity.class));
                finish();
                break;

            case R.id.nav_exit:
                getApp().getMainComponent().getEncryptedStorage().deletePassword();
                getApp().getMainComponent().getEncryptedStorage().getDefaultProject();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onSearchItemClicked(Issue issue) {
        openIssue(issue);
    }

    @Override
    public void onCaptureBoardFragmentInteraction() {
    }

    public SearchFragment getTicketsFragment() {
        return (SearchFragment) getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG);
    }

    @Override
    public void onPrintStatusRequested(UIIssueStatus issueStatus) {
        printStatus(issueStatus);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void renderUserDetails(User user) {
        imageLoader.loadImage(user.getAvatarUrls().get48x48(), this, userProfileImage);
        username.setText(user.getDisplayName());
        email.setText(user.getEmailAddress());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }
}
