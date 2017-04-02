package com.fullmob.jiraboard.managers.user;

import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.models.User;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class UserManager {

    private static final String KEY_JIRA_USER = "jira_user";
    private final LocalStorageInterface localStorage;
    private final JiraCloudUserManager jiraUserManager;
    private final SerializerInterface serializer;

    public UserManager(
        JiraCloudUserManager userManager,
        LocalStorageInterface localStorage,
        SerializerInterface serializer
    ) {
        this.serializer = serializer;
        this.jiraUserManager = userManager;
        this.localStorage = localStorage;
    }

    public Observable<Response<User>> fetchUserProfile() {
        return jiraUserManager.getProfile()
            .subscribeOn(Schedulers.io())
            .doOnNext(new Consumer<Response<User>>() {
                @Override
                public void accept(Response<User> userResponse) throws Exception {
                    User user = userResponse.body();
                    localStorage.putString(KEY_JIRA_USER, serializer.serialize(user));
                }
            });
    }

    public User getUser() {
        String user = localStorage.getString(KEY_JIRA_USER);

        return user != null ? serializer.deSerialize(user, User.class) : null;
    }
}
