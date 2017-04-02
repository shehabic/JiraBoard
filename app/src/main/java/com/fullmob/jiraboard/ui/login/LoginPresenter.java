package com.fullmob.jiraboard.ui.login;

import com.fullmob.jiraapi.models.User;
import com.fullmob.jiraapi.responses.AuthResponse;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.user.AuthenticationManager;
import com.fullmob.jiraboard.managers.user.UserManager;
import com.fullmob.jiraboard.schedulers.FullmobSchedulers;
import com.fullmob.jiraboard.ui.AbstractPresenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class LoginPresenter extends AbstractPresenter<LoginView> {
    private final AuthenticationManager authManager;
    private final EncryptedStorage encryptedStorage;
    private final UserManager userManager;

    public LoginPresenter(
        LoginView view,
        AuthenticationManager authManager,
        EncryptedStorage encryptedStorage,
        UserManager userManager
    ) {
        super(view);
        this.authManager = authManager;
        this.encryptedStorage = encryptedStorage;
        this.userManager = userManager;
    }

    void onViewCreated() {
        if (authManager.isAlreadyAuthorized()) {
            if (encryptedStorage.getDefaultProject() != null) {
                getView().proceedToIssueTypesScreen();
            } else {
                getView().proceedToProjectsScreen();
            }
        } else {
            String username = encryptedStorage.getUsername();
            String subDomain = encryptedStorage.getSubDomain();
            getView().preFillEmail(username != null ? username : "");
            getView().preFillSubDomain(subDomain != null ? subDomain : "");
        }
    }

    void onLoginRequested(String subDomain, String user, String password) {
        getView().showLoading();
        authManager.login(subDomain, user, password)
            .observeOn(FullmobSchedulers.getMainThread())
            .subscribe(new Observer<Response<AuthResponse>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    getView().showLoading();
                }

                @Override
                public void onNext(Response<AuthResponse> response) {
                    getView().hideLoading();
                    if (authManager.isSuccess(response.body()) && getView() != null) {
                        getView().reloadAfterLogin();
                    } else {
                        getView().showInvalidCredentials();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    getView().hideLoading();
                }

                @Override
                public void onComplete() {
                    getView().hideLoading();
                }
            });
    }

    public void onUserLoggedIn() {
        getView().showLoading();
        userManager.fetchUserProfile()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response<User>>() {
                @Override
                public void accept(Response<User> userResponse) throws Exception {
                    getView().proceedToProjectsScreen();
                    getView().hideLoading();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    getView().showUnknownError();
                    getView().hideLoading();
                }
            });


    }
}
