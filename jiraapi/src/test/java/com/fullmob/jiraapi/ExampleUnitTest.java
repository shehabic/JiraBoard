package com.fullmob.jiraapi;

import com.fullmob.jiraapi.api.AuthApi;
import com.fullmob.jiraapi.config.ApiConfig;
import com.fullmob.jiraapi.requests.LoginRequest;
import com.fullmob.jiraapi.responses.AuthResponse;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    int LOCK_WAIT_IN_SECONDS = 2;
    protected CountDownLatch lock = new CountDownLatch(1);

    @Test
    public void basicApiTest() throws Exception {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setBaseUrl("");
        apiConfig.setDebug(true);

        ApiClientBuilder<AuthApi> builder = new ApiClientBuilder(AuthApi.class);
        AuthApi api = builder.setHttpClient(new HttpClientBuilder(apiConfig).build()).setConfig(apiConfig).build();
        api.login(new LoginRequest("", ""))
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<Response<AuthResponse>>() {
            @Override
            public void accept(Response<AuthResponse> authResponseResponse) throws Exception {
                Assert.assertEquals(authResponseResponse.code(), 200);
                lock.countDown();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Assert.fail();
            }
        });
        lock.await(LOCK_WAIT_IN_SECONDS, TimeUnit.SECONDS);
    }
}