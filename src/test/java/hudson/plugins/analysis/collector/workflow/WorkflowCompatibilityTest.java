package hudson.plugins.analysis.collector.workflow;

import static org.junit.Assert.*;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.Result;

import hudson.plugins.analysis.collector.AnalysisResultAction;

import hudson.FilePath;

/**
 * Test workflow compatibility.
 */
public class WorkflowCompatibilityTest {

    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    /**
     * General collector test using Findbugsas example.
     */
    @Test
    @Ignore("This test requires FindBugs 4.62 or later. Ignoring until it is released")
    public void collectorPublisherWorkflowStep() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        job.setDefinition(new CpsFlowDefinition(
        "node {\n" +
        "  step([$class: 'FindBugsPublisher'])\n" +
        "  step([$class: 'AnalysisPublisher', isFindBugsActivated: true])\n" +
        "}"));
        j.assertBuildStatusSuccess(job.scheduleBuild2(0));
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        assertEquals(2, result.getResult().getAnnotations().size());
    }

    /**
     * Failure test.
     */
    @Test
    @Ignore("This test requires FindBugs 4.62 or later. Ignoring until it is released")
    public void collectorPublisherWorkflowStepSetLimits() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf2");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        job.setDefinition(new CpsFlowDefinition(
        "node {\n" +
        "  step([$class: 'FindBugsPublisher'])\n" +
        "  step([$class: 'AnalysisPublisher', failedTotalAll: '0', usePreviousBuildAsReference: false])\n" +
        "}"));
        j.assertBuildStatus(Result.FAILURE, job.scheduleBuild2(0).get());
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        assertEquals(result.getResult().getAnnotations().size(), 2);
    }

    /**
     * Unstable test.
     */
    @Test
    @Ignore("This test requires FindBugs 4.62 or later. Ignoring until it is released")
    public void collectorPublisherWorkflowStepFailure() throws Exception {
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "wf3");
        FilePath workspace = j.jenkins.getWorkspaceFor(job);
        FilePath report = workspace.child("target").child("findbugs.xml");
        report.copyFrom(WorkflowCompatibilityTest.class.getResourceAsStream("/findbugs-native.xml"));
        job.setDefinition(new CpsFlowDefinition(
        "node {\n" +
        "  step([$class: 'FindBugsPublisher'])\n" +
        "  step([$class: 'AnalysisPublisher', unstableTotalAll: '0', usePreviousBuildAsReference: false])\n" +
        "}"));
        j.assertBuildStatus(Result.UNSTABLE, job.scheduleBuild2(0).get());
        AnalysisResultAction result = job.getLastBuild().getAction(AnalysisResultAction.class);
        assertEquals(result.getResult().getAnnotations().size(), 2);
    }
}

