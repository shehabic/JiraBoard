package com.fullmob.jiraapi.managers;

import com.fullmob.jiraapi.api.UserApi;
import com.fullmob.jiraapi.models.User;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class JiraCloudUserManager extends AbstractApiManager<UserApi> {
    public JiraCloudUserManager(UserApi api) {
        super(api);
    }

    public Observable<Response<User>> getProfile() {
        return api.getProfile().subscribeOn(Schedulers.io());
    }
}
