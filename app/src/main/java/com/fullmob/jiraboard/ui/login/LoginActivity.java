package com.fullmob.jiraboard.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.home.HomeActivity;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesActivity;
import com.fullmob.jiraboard.ui.projects.ProjectsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A onLoginRequested screen that offers onLoginRequested linkId email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    // UI references.
    @BindView(R.id.email) EditText emailText;
    @BindView(R.id.password) EditText passwordText;
    @BindView(R.id.subdomain) EditText subDomainText;
    @OnClick(R.id.email_sign_in_button) void onClick() {
        attemptLogin();
    }

    @Inject LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        injectComponents();
        initUI();
        presenter.onViewCreated();
    }

    private void injectComponents() {
        getApp().createLoginScreenComponent(this).inject(this);
    }

    private void initUI() {
        ButterKnife.bind(this);
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptLogin() {
        // Reset errors.
        emailText.setError(null);
        passwordText.setError(null);
        subDomainText.setError(null);

        // Store values at the time of the onLoginRequested attempt.
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String domain = subDomainText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordText.setError(getString(R.string.error_invalid_password));
            focusView = passwordText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailText.setError(getString(R.string.error_field_required));
            focusView = emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailText.setError(getString(R.string.error_invalid_email));
            focusView = emailText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(domain) || !isSubDomainValid(domain)) {
            subDomainText.setError(getString(R.string.error_invalid_subdomain));
            focusView = subDomainText;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            presenter.onLoginRequested(domain, email, password);
        }
    }

    private boolean isSubDomainValid(String domain) {
        return !domain.contains("/") && !domain.contains("\\") && !domain.contains("_") && !domain.contains(":");
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void proceedToIssueHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    @Override
    public void proceedToIssueTypesScreen() {
        startActivity(new Intent(this, IssueTypesActivity.class));
        finish();
    }

    @Override
    public void proceedToProjectsScreen() {
        startActivity(new Intent(this, ProjectsActivity.class));
        finish();
    }

    @Override
    public void showInvalidCredentials() {
        showToast("Invalid login credentials");
    }

    @Override
    public void showNetworkError() {
        showToast("Network is not available");
    }

    @Override
    public void showUnknownError() {
        showToast("An unknown error has occurred");
    }

    @Override
    public void preFillEmail(String email) {
        this.emailText.setText(email);
    }

    @Override
    public void preFillSubDomain(String subDomain) {
        this.subDomainText.setText(subDomain);
    }

    @Override
    public void reloadAfterLogin() {
        getApp().reloadAfterUserLogin();
        injectComponents();
        presenter.onUserLoggedIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

