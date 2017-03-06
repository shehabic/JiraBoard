package com.fullmob.graphlib;

import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.managers.ProjectsManager;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ApiDiscoveryGraphTest extends BaseUnitTest {

    private WorkflowDiscovery discovery;

    @Before
    public void setup() {
        discovery = new WorkflowDiscovery(new IssuesManager(getIssuesApi()), new ProjectsManager(getProjectsApi()), true);
    }

    @Test
    public void TestLearningTheStatusesGraph() throws Exception {
        discovery.discoverWorkFlow(TEST_TICKET_ID).subscribe(new Consumer<DiscoveryStatus>() {
            @Override
            public void accept(DiscoveryStatus status) throws Exception {
                System.out.print(String.format("Passes %d ", status.getPasses()));
                System.out.print(String.format("Discovered %d node(s)", status.getUniqueNodes()));
                System.out.print(
                    String.format("Completed %d/%d \n", status.getCompletedNodesCount(), status.getPossibleNodesCount())
                );
                if (status.isCompleted()) {
                    System.out.println(new Gson().toJson(status));
                    lock.countDown();
                    for (TransitionLink link : status.getVertices()) {
                        System.out.print(link.from + "-----[" + link.linkId + "]------>" + link.to);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (!(throwable instanceof WorkflowDiscovery.BadEndException)) {
                    Assert.fail();
                    lock.countDown();
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                lock.countDown();
            }
        });
        lock.await(LOCK_WAIT_IN_SECONDS * 20, TimeUnit.SECONDS);
    }
}