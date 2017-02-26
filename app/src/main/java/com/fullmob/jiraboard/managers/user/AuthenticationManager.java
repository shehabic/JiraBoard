package com.fullmob.jiraboard.managers.user;

import com.fullmob.jiraapi.managers.AuthManager;
import com.fullmob.jiraapi.responses.AuthResponse;
import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class AuthenticationManager {

    private final AuthManager api;
    private final EncryptedStorage encStorage;

    public AuthenticationManager(AuthManager api, EncryptedStorage encStorage) {
        this.api = api;
        this.encStorage = encStorage;
    }

    public Observable<Response<AuthResponse>> login(final String subDomain, final String user, final String password) {
        return api.getAuthSession("https://" + subDomain + BuildConfig.JIRA_AUTH_URL, user, password)
            .doOnNext(new Consumer<Response<AuthResponse>>() {
                @Override
                public void accept(Response<AuthResponse> response) throws Exception {
                    if (isSuccess(response.body())) {
                        encStorage.saveSubDomain(subDomain);
                        encStorage.saveUsername(user);
                        encStorage.savePassword(password);
                    } else {
                        encStorage.deletePassword();
                    }
                }
            });
    }

    public boolean isSuccess(AuthResponse response) {
        return response != null && response.getSession() != null && response.getSession().getName() != null;
    }

    public boolean isAlreadyAuthorized() {
        return encStorage.getPassword() != null && encStorage.getSubDomain() != null && encStorage.getUsername() != null;
    }
}
