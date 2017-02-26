package com.fullmob.jiraboard.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fullmob.jiraboard.ui.home.QRActivity;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseActivity;

import javax.inject.Inject;

/**
 * A onLoginRequested screen that offers onLoginRequested via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    // UI references.
    private EditText emailText;
    private EditText passwordText;
    private EditText subDomainText;
    private View mProgressView;
    private View mLoginFormView;

    @Inject LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().createLoginScreenComponent(this).inject(this);
        presenter.onViewCreated();
        setContentView(R.layout.activity_login);
        // Set up the onLoginRequested form.
        subDomainText = (EditText) findViewById(R.id.subdomain);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the onLoginRequested form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual onLoginRequested attempt is made.
     */
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
            showProgress(true);
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

    /**
     * Shows the progress UI and hides the onLoginRequested form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void proceedToHomeScreen() {
        startActivity(new Intent(this, QRActivity.class));
        finish();
    }

    @Override
    public void showInvalidCredentials() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showUnknownError() {

    }

    @Override
    public void preFillEmail(String email) {

    }

    @Override
    public void preFillSubDomain(String subDomain) {

    }

    @Override
    public void showLoading() {
        showProgress(true);
    }

    @Override
    public void hideLoading() {
        showProgress(false);
    }
}

