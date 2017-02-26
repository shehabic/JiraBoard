package com.fullmob.jiraapi.managers;

import com.fullmob.jiraapi.api.AuthApi;
import com.fullmob.jiraapi.requests.LoginRequest;
import com.fullmob.jiraapi.responses.AuthResponse;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AuthManager extends AbstractApiManager<AuthApi> {
    public AuthManager(AuthApi authApi) {
        super(authApi);
    }

    public Observable<Response<AuthResponse>> getAuthSession(String cloudDomain, String username, String password) {
        return api.login(cloudDomain, new LoginRequest(username, password)).subscribeOn(Schedulers.io());
    }
}
