package com.fullmob.jiraapi;

import com.fullmob.graphlib.TransitionLink;
import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class ApiDiscoveryGraphTest extends BaseUnitTest {

    private WorkflowDiscovery discovery;

    @Before
    public void setup() {
        discovery = new WorkflowDiscovery(new IssuesManager(getIssuesApi()), true);
    }

    @Test
    public void TestLearningTheStatusesGraph() throws Exception {
        discovery.discoverWorkFlow(TEST_TICKET_ID).subscribe(new Consumer<HashSet<TransitionLink>>() {
            @Override
            public void accept(HashSet<TransitionLink> transitionLinks) throws Exception {
                System.out.println(new Gson().toJson(transitionLinks));
                lock.countDown();
                for (TransitionLink link : transitionLinks) {
                    System.out.print(link.from + "-----[" + link.via + "]------>" + link.to);
                }
            }
        });
        lock.await(LOCK_WAIT_IN_SECONDS * 20, TimeUnit.SECONDS);
    }
}