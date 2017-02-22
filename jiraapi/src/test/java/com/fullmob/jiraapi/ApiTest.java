package com.fullmob.jiraapi;

import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.requests.LoginRequest;
import com.fullmob.jiraapi.responses.AuthResponse;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ApiTest extends BaseUnitTest {

    @Test
    public void basicApiTest() throws Exception {
        getAuthApi().login(new LoginRequest(USERNAME, PASSWORD))
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

    @Test
    public void testTransitionTicket() throws Exception {
        IssuesManager issuesManager = new IssuesManager(getIssuesApi());
        issuesManager.moveIssue(TEST_TICKET_ID, "481").subscribe(new Consumer<Response<Issue>>() {
            @Override
            public void accept(Response<Issue> issueResponse) throws Exception {
                lock.countDown();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                lock.countDown();
            }
        });
        lock.await(LOCK_WAIT_IN_SECONDS, TimeUnit.SECONDS);
    }
}