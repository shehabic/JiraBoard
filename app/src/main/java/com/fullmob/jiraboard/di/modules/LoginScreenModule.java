package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.user.AuthenticationManager;
import com.fullmob.jiraboard.ui.login.LoginPresenter;
import com.fullmob.jiraboard.ui.login.LoginView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginScreenModule {
    private final WeakReference<LoginView> view;

    public LoginScreenModule(LoginView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public LoginPresenter providesLoginPresenter(AuthenticationManager authManager, EncryptedStorage encryptedStorage) {
        return new LoginPresenter(view.get(), authManager, encryptedStorage);
    }
}
